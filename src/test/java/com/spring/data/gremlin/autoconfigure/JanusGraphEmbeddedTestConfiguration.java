/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Slf4j
@TestConfiguration
public class JanusGraphEmbeddedTestConfiguration {

    @Bean
    @Primary
    public TypeSerializerRegistry janusGraphTypeSerializerRegistry() {
        log.info("Creating TypeSerializerRegistry with JanusGraph IoRegistry for testing");
        
        JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
        
        TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                .addRegistry(janusRegistry)
                .create();
                
        log.info("TypeSerializerRegistry with JanusGraph support created for testing");
        return registry;
    }
}
