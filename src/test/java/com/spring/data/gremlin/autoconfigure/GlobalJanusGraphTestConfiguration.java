/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@ConditionalOnClass(JanusGraphIoRegistry.class)
public class GlobalJanusGraphTestConfiguration {

    @Bean
    @Primary
    public TypeSerializerRegistry typeSerializerRegistry() {
        log.info("Creating global TypeSerializerRegistry with JanusGraph IoRegistry for all tests");
        
        JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
        
        TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                .addRegistry(janusRegistry)
                .create();
                
        log.info("Global TypeSerializerRegistry with JanusGraph support created for all tests");
        return registry;
    }
}
