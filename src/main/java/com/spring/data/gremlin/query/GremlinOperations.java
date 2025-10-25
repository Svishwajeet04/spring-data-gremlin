/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.query;

import com.spring.data.gremlin.common.GremlinEntityType;
import com.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.spring.data.gremlin.conversion.source.GremlinSource;
import com.spring.data.gremlin.query.query.GremlinQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Provider interface for basic Operations with Gremlin
 */
public interface GremlinOperations {

    void deleteAll();

    void deleteAll(GremlinEntityType type);

    <T> void deleteAll(GremlinSource<T> source);

    <T> boolean isEmptyGraph(GremlinSource<T> source);

    <T> boolean existsById(Object id, GremlinSource<T> source);

    <T> void deleteById(Object id, GremlinSource<T> source);

    <T> T insert(T object, GremlinSource<T> source);

    <T> T findById(Object id, GremlinSource<T> source);

    <T> T findVertexById(Object id, GremlinSource<T> source);

    <T> T findEdgeById(Object id, GremlinSource<T> source);

    <T> T update(T object, GremlinSource<T> source);

    <T> T save(T object, GremlinSource<T> source);

    <T> List<T> findAll(GremlinSource<T> source);

    long vertexCount();

    long edgeCount();

    <T> List<T> find(GremlinQuery query, GremlinSource<T> source);

    /**
     * Execute a raw Gremlin query and return a single result.
     * 
     * @param query the Gremlin query string
     * @return the query result as an Object
     */
    Object queryForObject(String query);

    /**
     * Execute a raw Gremlin query and return a single result with type conversion.
     * 
     * @param query the Gremlin query string
     * @param requiredType the required type for the result
     * @param <T> the type of the result
     * @return the query result converted to the specified type
     */
    <T> T queryForObject(String query, Class<T> requiredType);

    /**
     * Execute a raw Gremlin query and return a list of results.
     * 
     * @param query the Gremlin query string
     * @return the query results as a List of Objects
     */
    List<Object> queryForList(String query);

    /**
     * Execute a raw Gremlin query and return a list of results with type conversion.
     * 
     * @param query the Gremlin query string
     * @param requiredType the required type for the results
     * @param <T> the type of the results
     * @return the query results converted to the specified type
     */
    <T> List<T> queryForList(String query, Class<T> requiredType);

    /**
     * Execute a raw Gremlin query with pagination support.
     * 
     * @param query the Gremlin query string
     * @param offset the number of results to skip
     * @param limit the maximum number of results to return
     * @return the query results as a List of Objects
     */
    List<Object> queryForList(String query, int offset, int limit);

    /**
     * Execute a raw Gremlin query with pagination support and type conversion.
     * 
     * @param query the Gremlin query string
     * @param offset the number of results to skip
     * @param limit the maximum number of results to return
     * @param requiredType the required type for the results
     * @param <T> the type of the results
     * @return the query results converted to the specified type
     */
    <T> List<T> queryForList(String query, int offset, int limit, Class<T> requiredType);

    /**
     * Execute a raw Gremlin query with pagination support using Spring's Pageable.
     * 
     * @param query the Gremlin query string
     * @param pageable the pagination information
     * @return a Page containing the results and pagination metadata
     */
    Page<Object> queryForPage(String query, Pageable pageable);

    /**
     * Execute a raw Gremlin query with pagination support using Spring's Pageable and type conversion.
     * 
     * @param query the Gremlin query string
     * @param pageable the pagination information
     * @param requiredType the required type for the results
     * @param <T> the type of the results
     * @return a Page containing the results and pagination metadata
     */
    <T> Page<T> queryForPage(String query, Pageable pageable, Class<T> requiredType);

    MappingGremlinConverter getMappingConverter();
}
