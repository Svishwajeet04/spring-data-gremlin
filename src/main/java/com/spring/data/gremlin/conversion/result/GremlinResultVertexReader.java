/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.conversion.result;

import com.spring.data.gremlin.common.GremlinUtils;
import com.spring.data.gremlin.conversion.source.GremlinSource;
import com.spring.data.gremlin.conversion.source.GremlinSourceVertex;
import com.spring.data.gremlin.exception.GremlinUnexpectedSourceTypeException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.util.detached.DetachedVertex;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.spring.data.gremlin.common.Constants.*;

@Slf4j
@NoArgsConstructor
public class GremlinResultVertexReader extends AbstractGremlinResultReader implements GremlinResultsReader {

    private void validate(List<Result> results, GremlinSource source) {
        Assert.notNull(results, "Results should not be null.");
        Assert.notNull(source, "GremlinSource should not be null.");
        Assert.isTrue(results.size() == 1, "Vertex should contain only one result.");

        final Result result = results.get(0);
        final Object obj = result.getObject();
        
        // Debug logging to see what we're getting
        log.debug("GremlinResultVertexReader - Result object type: {}", obj.getClass().getName());
        log.debug("GremlinResultVertexReader - Result object: {}", obj);
        
        // Handle both Map and DetachedVertex objects
        if (obj instanceof DetachedVertex) {
            log.debug("GremlinResultVertexReader - Received DetachedVertex, which is acceptable");
            return;
        } else if (obj instanceof Map) {
            log.debug("GremlinResultVertexReader - Received Map, validating required properties");
            @SuppressWarnings("unchecked") final Map<String, Object> map = (Map<String, Object>) obj;
            
            Assert.isTrue(map.containsKey(PROPERTY_ID), "should contain id property");
            Assert.isTrue(map.containsKey(PROPERTY_LABEL), "should contain label property");
            Assert.isTrue(map.containsKey(PROPERTY_TYPE), "should contain type property");
            Assert.isTrue(map.containsKey(PROPERTY_PROPERTIES), "should contain properties property");
            Assert.isTrue(map.get(PROPERTY_TYPE).equals(RESULT_TYPE_VERTEX), "must be vertex type");
            Assert.isInstanceOf(Map.class, map.get(PROPERTY_PROPERTIES), "should be one instance of Map");
        } else {
            log.error("GremlinResultVertexReader - Unexpected result type: {}", obj.getClass().getName());
            throw new IllegalArgumentException("Result should be either a Map or DetachedVertex, but was: " + obj.getClass());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(List<Result> results, GremlinSource source) {
        if (!(source instanceof GremlinSourceVertex)) {
            throw new GremlinUnexpectedSourceTypeException("Should be instance of GremlinSourceVertex");
        }

        validate(results, source);

        final Result result = results.get(0);
        final Object obj = result.getObject();
        
        log.debug("GremlinResultVertexReader - Processing result object: {}", obj);

        if (obj instanceof DetachedVertex) {
            // Handle DetachedVertex
            final DetachedVertex vertex = (DetachedVertex) obj;
            log.debug("GremlinResultVertexReader - Processing DetachedVertex with ID: {}, Label: {}", vertex.id(), vertex.label());
            
            source.setId(vertex.id());
            source.setLabel(vertex.label());
            
            // Set properties from the vertex
            vertex.properties().forEachRemaining(prop -> {
                log.debug("GremlinResultVertexReader - Setting property: {} = {}", prop.key(), prop.value());
                source.setProperty(prop.key(), prop.value());
            });
            
            // Set the classname if it exists
            if (source.getProperties().containsKey(GREMLIN_PROPERTY_CLASSNAME)) {
                final String className = source.getProperties().get(GREMLIN_PROPERTY_CLASSNAME).toString();
                log.debug("GremlinResultVertexReader - Setting classname: {}", className);
                source.setIdField(GremlinUtils.getIdField(GremlinUtils.toEntityClass(className)));
            }
        } else if (obj instanceof Map) {
            // Handle Map format (original logic)
            final Map<String, Object> map = (Map<String, Object>) obj;
            final Map<String, Object> properties = (Map<String, Object>) map.get(PROPERTY_PROPERTIES);
            
            log.debug("GremlinResultVertexReader - Processing Map with properties: {}", properties);

            super.readResultProperties(properties, source);

            final String className = source.getProperties().get(GREMLIN_PROPERTY_CLASSNAME).toString();

            source.setIdField(GremlinUtils.getIdField(GremlinUtils.toEntityClass(className)));
            source.setId(map.get(PROPERTY_ID));
            source.setLabel(map.get(PROPERTY_LABEL).toString());
        }
        
        log.debug("GremlinResultVertexReader - Final source properties: {}", source.getProperties());
    }
}
