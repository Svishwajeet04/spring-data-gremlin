/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.tinkerpop.gremlin.util.ser.Serializers;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("gremlin")
public class TestGremlinProperties {
    private String endpoint;

    private int port;

    private String username;

    private String password;

    private boolean sslEnabled = true;

    private String serializer = Serializers.GRAPHSON.toString();

    public TestGremlinProperties() {
    }

    public TestGremlinProperties(String endpoint, int port, String username, String password, boolean sslEnabled, String serializer) {
        this.endpoint = endpoint;
        this.port = port;
        this.username = username;
        this.password = password;
        this.sslEnabled = sslEnabled;
        this.serializer = serializer;
    }

}