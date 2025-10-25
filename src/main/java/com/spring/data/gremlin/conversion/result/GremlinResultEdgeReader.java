/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.conversion.result;

import com.spring.data.gremlin.common.GremlinUtils;
import com.spring.data.gremlin.conversion.source.GremlinSource;
import com.spring.data.gremlin.conversion.source.GremlinSourceEdge;
import com.spring.data.gremlin.exception.GremlinUnexpectedSourceTypeException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.util.detached.DetachedEdge;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.spring.data.gremlin.common.Constants.*;

@Slf4j
@NoArgsConstructor
public class GremlinResultEdgeReader extends AbstractGremlinResultReader implements GremlinResultsReader {

    private void readProperties(@NonNull GremlinSource source, @Nullable Map map) {
        if (map != null) {
            @SuppressWarnings("unchecked") final Map<String, Object> properties = (Map<String, Object>) map;

            properties.forEach(source::setProperty);
        }
    }

    private void validate(List<Result> results, GremlinSource source) {
        Assert.notNull(results, "Results should not be null.");
        Assert.notNull(source, "GremlinSource should not be null.");
        Assert.isTrue(results.size() == 1, "Edge should contain only one result.");

        final Result result = results.get(0);
        final Object obj = result.getObject();
        
        // Debug logging to see what we're getting
        log.debug("GremlinResultEdgeReader - Result object type: {}", obj.getClass().getName());
        log.debug("GremlinResultEdgeReader - Result object: {}", obj);
        
        // Handle both Map and DetachedEdge objects
        if (obj instanceof DetachedEdge) {
            log.debug("GremlinResultEdgeReader - Received DetachedEdge, which is acceptable");
            return;
        } else if (obj instanceof Map) {
            log.debug("GremlinResultEdgeReader - Received Map, validating required properties");
            @SuppressWarnings("unchecked") final Map<String, Object> map = (Map<String, Object>) obj;
            
            Assert.isTrue(map.containsKey(PROPERTY_ID), "should contain id property");
            Assert.isTrue(map.containsKey(PROPERTY_LABEL), "should contain label property");
            Assert.isTrue(map.containsKey(PROPERTY_TYPE), "should contain type property");
            Assert.isTrue(map.containsKey(PROPERTY_INV), "should contain inV property");
            Assert.isTrue(map.containsKey(PROPERTY_OUTV), "should contain outV property");
            Assert.isTrue(map.get(PROPERTY_TYPE).equals(RESULT_TYPE_EDGE), "must be edge type");
        } else {
            log.error("GremlinResultEdgeReader - Unexpected result type: {}", obj.getClass().getName());
            throw new IllegalArgumentException("Result should be either a Map or DetachedEdge, but was: " + obj.getClass());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(List<Result> results, GremlinSource source) {
        if (!(source instanceof GremlinSourceEdge)) {
            throw new GremlinUnexpectedSourceTypeException("Should be instance of GremlinSourceEdge");
        }

        validate(results, source);

        final GremlinSourceEdge sourceEdge = (GremlinSourceEdge) source;
        final Result result = results.get(0);
        final Object obj = result.getObject();
        
        log.debug("GremlinResultEdgeReader - Processing result object: {}", obj);

        if (obj instanceof DetachedEdge) {
            // Handle DetachedEdge
            final DetachedEdge edge = (DetachedEdge) obj;
            log.debug("GremlinResultEdgeReader - Processing DetachedEdge with ID: {}, Label: {}", edge.id(), edge.label());
            
            sourceEdge.setId(edge.id());
            sourceEdge.setLabel(edge.label());
            
            // Set properties from the edge
            edge.properties().forEachRemaining(prop -> {
                log.debug("GremlinResultEdgeReader - Setting property: {} = {}", prop.key(), prop.value());
                source.setProperty(prop.key(), prop.value());
            });
            
            // Set vertex IDs
            sourceEdge.setVertexIdFrom(edge.outVertex().id());
            sourceEdge.setVertexIdTo(edge.inVertex().id());
            
            // Set the classname if it exists
            if (source.getProperties().containsKey(GREMLIN_PROPERTY_CLASSNAME)) {
                final String className = source.getProperties().get(GREMLIN_PROPERTY_CLASSNAME).toString();
                log.debug("GremlinResultEdgeReader - Setting classname: {}", className);
                sourceEdge.setIdField(GremlinUtils.getIdField(GremlinUtils.toEntityClass(className)));
            }
        } else if (obj instanceof Map) {
            // Handle Map format (original logic)
            final Map<String, Object> map = (Map<String, Object>) obj;
            
            log.debug("GremlinResultEdgeReader - Processing Map with properties: {}", map);

            this.readProperties(source, (Map) map.get(PROPERTY_PROPERTIES));

            final String className = source.getProperties().get(GREMLIN_PROPERTY_CLASSNAME).toString();

            sourceEdge.setIdField(GremlinUtils.getIdField(GremlinUtils.toEntityClass(className)));
            sourceEdge.setId(map.get(PROPERTY_ID));
            sourceEdge.setLabel(map.get(PROPERTY_LABEL).toString());
            sourceEdge.setVertexIdFrom(map.get(PROPERTY_OUTV));
            sourceEdge.setVertexIdTo(map.get(PROPERTY_INV));
        }
        
        log.debug("GremlinResultEdgeReader - Final source properties: {}", source.getProperties());
    }
}
