/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query.query;

import com.microsoft.spring.data.gremlin.annotation.GremlinQuery;
import com.microsoft.spring.data.gremlin.query.GremlinEntityMetadata;
import com.microsoft.spring.data.gremlin.query.SimpleGremlinEntityMetadata;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

import java.lang.reflect.Method;
import java.util.Optional;

public class GremlinQueryMethod extends QueryMethod {

    private GremlinEntityMetadata<?> metadata;
    private final Method sourceMethod;

    public GremlinQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        super(method, metadata, factory);
        this.sourceMethod = method;
    }

    @Override
    public EntityMetadata<?> getEntityInformation() {
        @SuppressWarnings("unchecked") final Class<Object> domainClass = (Class<Object>) super.getDomainClass();

        this.metadata = new SimpleGremlinEntityMetadata<>(domainClass);

        return this.metadata;
    }

    public Method getSourceMethod() {
        return this.sourceMethod;
    }

    public boolean hasAnnotatedQuery() {
        return getAnnotatedQuery().isPresent();
    }

    public Optional<String> getAnnotatedQuery() {
        GremlinQuery annotation = this.sourceMethod.getAnnotation(GremlinQuery.class);
        return annotation == null ? Optional.empty() : Optional.of(annotation.value());
    }
}
