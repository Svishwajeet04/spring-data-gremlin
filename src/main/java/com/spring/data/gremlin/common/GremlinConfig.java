/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common;

import lombok.*;
import org.apache.tinkerpop.gremlin.util.ser.Serializers;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GremlinConfig {
    private String endpoint;

    private int port;

    private String username;

    private String password;

    private boolean sslEnabled;

    private String serializer;

    private int maxContentLength;
    
    private TypeSerializerRegistry typeSerializerRegistry;

    private GremlinConfig(GremlinConfigBuilder builder) {
        this.endpoint = builder.endpoint;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.sslEnabled = builder.sslEnabled;
        this.serializer = builder.serializer;
        this.maxContentLength = builder.maxContentLength;
        this.typeSerializerRegistry = builder.typeSerializerRegistry;
    }

    public static GremlinConfigBuilder builder() {
        return new GremlinConfigBuilder();
    }

    public static GremlinConfigBuilder builder(String endpoint, String username, String password) {
        return new GremlinConfigBuilder()
                .endpoint(endpoint)
                .username(username)
                .password(password)
                .port(Constants.DEFAULT_ENDPOINT_PORT)
                .sslEnabled(true)
                .serializer(Serializers.GRAPHSON.toString());
    }

    public static class GremlinConfigBuilder {
        private String endpoint;
        private int port;
        private String username;
        private String password;
        private boolean sslEnabled;
        private String serializer;
        private int maxContentLength;
        private TypeSerializerRegistry typeSerializerRegistry;

        public GremlinConfigBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public GremlinConfigBuilder port(int port) {
            this.port = port;
            return this;
        }

        public GremlinConfigBuilder username(String username) {
            this.username = username;
            return this;
        }

        public GremlinConfigBuilder password(String password) {
            this.password = password;
            return this;
        }

        public GremlinConfigBuilder sslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
            return this;
        }

        public GremlinConfigBuilder serializer(String serializer) {
            this.serializer = serializer;
            return this;
        }

        public GremlinConfigBuilder maxContentLength(int maxContentLength) {
            this.maxContentLength = maxContentLength;
            return this;
        }
        
        public GremlinConfigBuilder typeSerializerRegistry(TypeSerializerRegistry typeSerializerRegistry) {
            this.typeSerializerRegistry = typeSerializerRegistry;
            return this;
        }

        public GremlinConfig build() {
            return new GremlinConfig(this);
        }
    }
}
