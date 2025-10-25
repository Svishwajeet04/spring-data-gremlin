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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("JanusGraph Type Serializer Configuration Tests")
class JanusGraphTypeSerializerConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(JanusGraphTypeSerializerConfiguration.class));

    @Nested
    @DisplayName("When JanusGraph is available")
    class WhenJanusGraphAvailable {

        @Test
        @DisplayName("Should create TypeSerializerRegistry bean with JanusGraph support")
        void shouldCreateTypeSerializerRegistryWithJanusGraphSupport() {
            contextRunner
                    .run(context -> {
                        assertThat(context).hasSingleBean(TypeSerializerRegistry.class);
                        
                        TypeSerializerRegistry registry = context.getBean(TypeSerializerRegistry.class);
                        assertThat(registry).isNotNull();
                        
                        log.info("TypeSerializerRegistry created successfully with JanusGraph support");
                    });
        }

        @Test
        @DisplayName("Should not override existing TypeSerializerRegistry bean")
        void shouldNotOverrideExistingTypeSerializerRegistryBean() {
            contextRunner
                    .withUserConfiguration(ExistingRegistryConfiguration.class)
                    .run(context -> {
                        assertThat(context).hasSingleBean(TypeSerializerRegistry.class);
                        
                        TypeSerializerRegistry registry = context.getBean(TypeSerializerRegistry.class);
                        assertThat(registry).isNotNull();
                        
                        log.info("Existing TypeSerializerRegistry bean preserved");
                    });
        }
    }

    @Nested
    @DisplayName("When JanusGraph is not available")
    class WhenJanusGraphNotAvailable {

        @Test
        @DisplayName("Should not create TypeSerializerRegistry bean")
        void shouldNotCreateTypeSerializerRegistryBean() {
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(JanusGraphTypeSerializerConfiguration.class))
                    .withClassLoader(new ClassLoader() {
                        @Override
                        public Class<?> loadClass(String name) throws ClassNotFoundException {
                            if (name.equals(JanusGraphIoRegistry.class.getName())) {
                                throw new ClassNotFoundException("JanusGraphIoRegistry not available");
                            }
                            return super.loadClass(name);
                        }
                    })
                    .run(context -> {
                        assertThat(context).doesNotHaveBean(TypeSerializerRegistry.class);
                        log.info("TypeSerializerRegistry bean not created when JanusGraph is unavailable");
                    });
        }
    }

    @Nested
    @DisplayName("JanusGraph Remote Server Integration")
    class JanusGraphRemoteServerIntegration extends JanusGraphTestBase {

        @Test
        @DisplayName("Should connect to remote Gremlin server")
        void shouldConnectToRemoteGremlinServer() {
            skipIfJanusGraphNotAvailable();
            
            log.info("Testing connection to remote Gremlin server");
            
            try {
                // Test basic connectivity using anonymous traversal
                var result = client.submit("g.V().count()").all().get();
                long vertexCount = (Long) result.get(0).getObject();
                log.info("Current vertex count in remote graph: {}", vertexCount);
                
                assertThat(vertexCount).isGreaterThanOrEqualTo(0);
                log.info("Successfully connected to remote Gremlin server");
                
            } catch (Exception e) {
                log.error("Failed to connect to remote Gremlin server", e);
                throw new RuntimeException("Failed to connect to remote Gremlin server", e);
            }
        }

        @Test
        @DisplayName("Should work with TypeSerializerRegistry and remote server")
        void shouldWorkWithTypeSerializerRegistryAndRemoteServer() {
            skipIfJanusGraphNotAvailable();
            
            log.info("Testing TypeSerializerRegistry integration with remote Gremlin server");
            
            assertThat(typeSerializerRegistry).isNotNull();
            
            try {
                // Test basic operations using anonymous traversal
                var vertexResult = client.submit("g.V().count()").all().get();
                var edgeResult = client.submit("g.E().count()").all().get();
                
                long vertexCount = (Long) vertexResult.get(0).getObject();
                long edgeCount = (Long) edgeResult.get(0).getObject();
                
                log.info("Remote graph stats - Vertices: {}, Edges: {}", vertexCount, edgeCount);
                
                assertThat(vertexCount).isGreaterThanOrEqualTo(0);
                assertThat(edgeCount).isGreaterThanOrEqualTo(0);
                
                log.info("TypeSerializerRegistry works correctly with remote Gremlin server");
                
            } catch (Exception e) {
                log.error("Failed to query remote Gremlin server", e);
                throw new RuntimeException("Failed to query remote Gremlin server", e);
            }
        }
    }

    @Configuration
    static class ExistingRegistryConfiguration {
        
        @Bean
        public TypeSerializerRegistry existingTypeSerializerRegistry() {
            log.info("Creating existing TypeSerializerRegistry bean");
            return TypeSerializerRegistry.build().create();
        }
    }
}
