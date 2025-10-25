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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(JanusGraphIoRegistry.class)
@Slf4j
public class JanusGraphTypeSerializerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TypeSerializerRegistry typeSerializerRegistry() {
        log.info("Creating TypeSerializerRegistry with JanusGraph IoRegistry support");
        
        JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
        
        TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                .addRegistry(janusRegistry)
                .create();
                
        log.info("Successfully configured TypeSerializerRegistry with JanusGraph support");
        return registry;
    }
}
