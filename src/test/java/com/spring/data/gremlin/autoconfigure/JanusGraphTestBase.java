/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestConfiguration
@TestPropertySource(properties = {
    "spring.data.gremlin.endpoint=localhost",
    "spring.data.gremlin.port=8182",
    "spring.data.gremlin.username=",
    "spring.data.gremlin.password=",
    "spring.data.gremlin.ssl-enabled=false",
    "spring.data.gremlin.serializer=GRAPHBINARY_V1"
})
public class JanusGraphTestBase {

    protected Cluster cluster;
    protected Client client;
    protected TypeSerializerRegistry typeSerializerRegistry;
    protected boolean supportsUserSuppliedIds = false;

    @Bean
    @Primary
    public TypeSerializerRegistry janusGraphTypeSerializerRegistry() {
        log.info("Creating TypeSerializerRegistry with JanusGraph IoRegistry for test");
        
        JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
        
        TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                .addRegistry(janusRegistry)
                .create();
                
        log.info("TypeSerializerRegistry with JanusGraph support created for test");
        return registry;
    }

    @BeforeEach
    void setUpJanusGraphConnection() {
        log.info("Setting up JanusGraph connection to localhost:8182");
        
        try {
            cluster = Cluster.build()
                    .addContactPoint("localhost")
                    .port(8182)
                    .create();
            
            client = cluster.connect();
            
            // Create TypeSerializerRegistry with JanusGraph IoRegistry
            JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
            typeSerializerRegistry = TypeSerializerRegistry.build()
                    .addRegistry(janusRegistry)
                    .create();
            
            // Test if server supports user-supplied IDs
            testUserSuppliedIdSupport();
            
            log.info("Successfully connected to JanusGraph server at localhost:8182 with TypeSerializerRegistry");
            log.info("Server supports user-supplied IDs: {}", supportsUserSuppliedIds);
            
        } catch (Exception e) {
            log.warn("Could not connect to JanusGraph server at localhost:8182: {}", e.getMessage());
            cluster = null;
            client = null;
            typeSerializerRegistry = null;
        }
    }

    private void testUserSuppliedIdSupport() {
        try {
            // Try to create a vertex with explicit ID
            var result = client.submit("g.addV('test').property(T.id, 'test-id-123').next()").all().get();
            supportsUserSuppliedIds = true;
            log.info("Server supports user-supplied vertex IDs");
            
            // Clean up test vertex
            client.submit("g.V('test-id-123').drop()").all().get();
            
        } catch (Exception e) {
            supportsUserSuppliedIds = false;
            log.info("Server does not support user-supplied vertex IDs: {}", e.getMessage());
        }
    }

    @AfterEach
    void tearDownJanusGraphConnection() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                log.warn("Error closing JanusGraph client", e);
            }
        }
        if (cluster != null) {
            try {
                cluster.close();
            } catch (Exception e) {
                log.warn("Error closing JanusGraph cluster", e);
            }
        }
    }

    protected boolean isJanusGraphAvailable() {
        return client != null && cluster != null && typeSerializerRegistry != null;
    }

    protected void skipIfJanusGraphNotAvailable() {
        if (!isJanusGraphAvailable()) {
            log.info("Skipping test - JanusGraph server not available");
            return;
        }
    }

    protected void skipIfUserSuppliedIdsNotSupported() {
        if (!supportsUserSuppliedIds) {
            log.info("Skipping test - JanusGraph server does not support user-supplied IDs");
            return;
        }
    }
}