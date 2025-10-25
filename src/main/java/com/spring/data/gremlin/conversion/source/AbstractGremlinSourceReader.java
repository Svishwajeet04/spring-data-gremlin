/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.conversion.source;

import com.spring.data.gremlin.common.GremlinUtils;
import com.spring.data.gremlin.exception.GremlinEntityInformationException;
import com.spring.data.gremlin.exception.GremlinUnexpectedEntityTypeException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.structure.util.detached.DetachedEdge;
import org.apache.tinkerpop.gremlin.structure.util.detached.DetachedVertex;
import org.apache.tinkerpop.shaded.jackson.databind.JavaType;
import org.apache.tinkerpop.shaded.jackson.databind.type.TypeFactory;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public abstract class AbstractGremlinSourceReader {

    protected Object readProperty(@NonNull PersistentProperty property, @Nullable Object value) {
        final Class<?> type = property.getTypeInformation().getType();
        final JavaType javaType = TypeFactory.defaultInstance().constructType(property.getType());

        log.debug("AbstractGremlinSourceReader.readProperty - Property: {}, Type: {}, Value: {} (class: {})", 
                  property.getName(), type.getSimpleName(), value, value != null ? value.getClass().getSimpleName() : "null");

        if (value == null) {
            log.debug("AbstractGremlinSourceReader.readProperty - Returning null for null value");
            return null;
        } else if (type == int.class || type == Integer.class
                || type == Boolean.class || type == boolean.class
                || type == String.class) {
            log.debug("AbstractGremlinSourceReader.readProperty - Returning primitive/wrapper type: {}", value);
            return value;
        } else if (type == Date.class) {
            Assert.isTrue(value instanceof Long, "Date store value must be instance of long");
            log.debug("AbstractGremlinSourceReader.readProperty - Converting Long to Date: {}", value);
            return new Date((Long) value);
        } else if (type == LocalDateTime.class) {
            log.debug("AbstractGremlinSourceReader.readProperty - Converting to LocalDateTime: {}", value);
            if (value instanceof String) {
                try {
                    // Try parsing as ISO-8601 format first
                    return LocalDateTime.parse((String) value);
                } catch (Exception e) {
                    log.warn("AbstractGremlinSourceReader.readProperty - Failed to parse LocalDateTime as ISO-8601, trying default format: {}", e.getMessage());
                    try {
                        // Try with default formatter
                        return LocalDateTime.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (Exception e2) {
                        log.error("AbstractGremlinSourceReader.readProperty - Failed to parse LocalDateTime string '{}': {}", value, e2.getMessage());
                        throw new GremlinUnexpectedEntityTypeException("Failed to parse LocalDateTime: " + value, e2);
                    }
                }
            } else {
                log.error("AbstractGremlinSourceReader.readProperty - LocalDateTime value must be String, got: {}", value.getClass().getSimpleName());
                throw new GremlinUnexpectedEntityTypeException("LocalDateTime value must be String, got: " + value.getClass().getSimpleName());
            }
        } else {
            log.debug("AbstractGremlinSourceReader.readProperty - Attempting to parse complex type: {} with value: {}", type.getSimpleName(), value);
            final Object object;

            try {
                log.debug("AbstractGremlinSourceReader.readProperty - Calling ObjectMapper.readValue with value: '{}' and type: {}", value.toString(), javaType);
                object = GremlinUtils.getObjectMapper().readValue(value.toString(), javaType);
                log.debug("AbstractGremlinSourceReader.readProperty - Successfully parsed to: {}", object);
            } catch (IOException e) {
                log.error("AbstractGremlinSourceReader.readProperty - Failed to parse value '{}' to type {}: {}", 
                         value.toString(), type.getSimpleName(), e.getMessage());
                log.error("AbstractGremlinSourceReader.readProperty - Full exception details:", e);
                throw new GremlinUnexpectedEntityTypeException("Failed to read String to Object", e);
            }

            return object;
        }
    }

    protected Object getGremlinSourceId(@NonNull GremlinSource source) {
        if (!source.getId().isPresent()) {
            return null;
        }

        final Object id = source.getId().get();
        final Field idField = source.getIdField();
        
        log.debug("AbstractGremlinSourceReader - Processing ID: {} of type: {}", id, id.getClass().getSimpleName());

        if (idField.getType() == String.class) {
            return id.toString();
        } else if (idField.getType() == Integer.class) {
            Assert.isTrue(id instanceof Integer, "source Id should be Integer.");
            return id;
        } else if (idField.getType() == Long.class && id instanceof Integer) {
            return Long.valueOf((Integer) id);
        } else if (idField.getType() == Long.class && id instanceof Long) {
            return id;
        } else if (id instanceof DetachedVertex) {
            // Handle DetachedVertex ID
            log.debug("AbstractGremlinSourceReader - Converting DetachedVertex ID to String: {}", id);
            return id.toString();
        } else if (id instanceof DetachedEdge) {
            // Handle DetachedEdge ID
            log.debug("AbstractGremlinSourceReader - Converting DetachedEdge ID to String: {}", id);
            return id.toString();
        } else {
            // Generic fallback: convert any unknown ID type to String
            log.debug("AbstractGremlinSourceReader - Converting unknown ID type {} to String: {}", id.getClass().getSimpleName(), id);
            return id.toString();
        }
    }
}

