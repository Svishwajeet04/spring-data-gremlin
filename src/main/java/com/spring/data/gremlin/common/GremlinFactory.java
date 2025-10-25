/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common;

import com.spring.data.gremlin.exception.GremlinIllegalConfigurationException;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.util.ser.GraphBinaryMessageSerializerV1;
import org.apache.tinkerpop.gremlin.util.ser.Serializers;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;

public class GremlinFactory {

    private Cluster gremlinCluster;

    private GremlinConfig gremlinConfig;

    public GremlinFactory(@NonNull GremlinConfig gremlinConfig) {
        final int port = gremlinConfig.getPort();
        if (port <= 0 || port > 65535) {
            gremlinConfig.setPort(Constants.DEFAULT_ENDPOINT_PORT);
        }
        
        final int maxContentLength = gremlinConfig.getMaxContentLength();
        if (maxContentLength <= 0) {
            gremlinConfig.setMaxContentLength(Constants.DEFAULT_MAX_CONTENT_LENGTH);
        }

        this.gremlinConfig = gremlinConfig;
    }

    private Cluster createGremlinCluster() {
        final Cluster cluster;

        try {
            Cluster.Builder builder = Cluster.build(this.gremlinConfig.getEndpoint())
                    .credentials(this.gremlinConfig.getUsername(), this.gremlinConfig.getPassword())
                    .enableSsl(this.gremlinConfig.isSslEnabled())
                    .maxContentLength(this.gremlinConfig.getMaxContentLength())
                    .port(this.gremlinConfig.getPort());
            
            // Use custom TypeSerializerRegistry if provided, otherwise use default serializer
            if (this.gremlinConfig.getTypeSerializerRegistry() != null && 
                Serializers.GRAPHBINARY_V1.toString().equals(this.gremlinConfig.getSerializer())) {
                // Create custom GraphBinary serializer with the provided TypeSerializerRegistry
                GraphBinaryMessageSerializerV1 customSerializer = 
                    new GraphBinaryMessageSerializerV1(this.gremlinConfig.getTypeSerializerRegistry());
                builder.serializer(customSerializer);
            } else {
                // Use default serializer
                builder.serializer(Serializers.valueOf(this.gremlinConfig.getSerializer()).simpleInstance());
            }
            
            cluster = builder.create();
        } catch (IllegalArgumentException e) {
            throw new GremlinIllegalConfigurationException("Invalid configuration of Gremlin", e);
        }

        return cluster;
    }

    public Client getGremlinClient() {

        if (this.gremlinCluster == null) {
            this.gremlinCluster = this.createGremlinCluster();
        }

        return this.gremlinCluster.connect();
    }
}
