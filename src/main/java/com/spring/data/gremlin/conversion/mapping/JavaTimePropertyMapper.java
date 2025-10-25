/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.conversion.mapping;

import com.spring.data.gremlin.conversion.source.GremlinSource;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Property mapper for Java 8 time types (LocalDateTime, LocalDate, LocalTime).
 * Handles proper serialization and deserialization of time objects.
 */
@Slf4j
@NoArgsConstructor
public class JavaTimePropertyMapper {

    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter ISO_LOCAL_TIME = DateTimeFormatter.ISO_LOCAL_TIME;

    /**
     * Check if the property type is a Java 8 time type.
     */
    public boolean supports(@NonNull Class<?> propertyType) {
        return LocalDateTime.class.isAssignableFrom(propertyType) ||
               LocalDate.class.isAssignableFrom(propertyType) ||
               LocalTime.class.isAssignableFrom(propertyType);
    }

    /**
     * Convert a Java 8 time object to a string representation for storage.
     */
    @NonNull
    public String toPropertyValue(@NonNull Object value) {
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(ISO_LOCAL_DATE_TIME);
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).format(ISO_LOCAL_DATE);
        } else if (value instanceof LocalTime) {
            return ((LocalTime) value).format(ISO_LOCAL_TIME);
        }
        
        log.warn("JavaTimePropertyMapper - Unsupported time type: {}", value.getClass().getSimpleName());
        return value.toString();
    }

    /**
     * Convert a string representation back to a Java 8 time object.
     */
    @Nullable
    public Object fromPropertyValue(@NonNull String value, @NonNull Class<?> targetType) {
        try {
            if (LocalDateTime.class.isAssignableFrom(targetType)) {
                return LocalDateTime.parse(value, ISO_LOCAL_DATE_TIME);
            } else if (LocalDate.class.isAssignableFrom(targetType)) {
                return LocalDate.parse(value, ISO_LOCAL_DATE);
            } else if (LocalTime.class.isAssignableFrom(targetType)) {
                return LocalTime.parse(value, ISO_LOCAL_TIME);
            }
        } catch (DateTimeParseException e) {
            log.warn("JavaTimePropertyMapper - Failed to parse time value '{}' as {}: {}", 
                    value, targetType.getSimpleName(), e.getMessage());
        }
        
        return null;
    }

    /**
     * Set a Java 8 time property on a GremlinSource.
     */
    public void setProperty(@NonNull GremlinSource source, @NonNull String propertyName, @NonNull Object value) {
        if (supports(value.getClass())) {
            String stringValue = toPropertyValue(value);
            log.debug("JavaTimePropertyMapper - Setting time property {} = {} (as string: {})", 
                    propertyName, value, stringValue);
            source.setProperty(propertyName, stringValue);
        }
    }

    /**
     * Get a Java 8 time property from a GremlinSource.
     */
    @Nullable
    public Object getProperty(@NonNull GremlinSource source, @NonNull String propertyName, @NonNull Class<?> targetType) {
        Object value = source.getProperties().get(propertyName);
        if (value instanceof String && supports(targetType)) {
            Object parsedValue = fromPropertyValue((String) value, targetType);
            log.debug("JavaTimePropertyMapper - Getting time property {} = {} (parsed from: {})", 
                    propertyName, parsedValue, value);
            return parsedValue;
        }
        return value;
    }
}
