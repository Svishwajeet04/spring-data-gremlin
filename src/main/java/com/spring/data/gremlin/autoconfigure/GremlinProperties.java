package com.spring.data.gremlin.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tinkerpop.gremlin.util.ser.Serializers;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("gremlin.client")
public class GremlinProperties {
    private String endpoint = "localhost";
    private int port = 8182;
    private String username = null;
    private String password = null;
    private boolean sslEnabled = false;
    private boolean telemetryAllowed = true;
    // Default to GraphBinary for JanusGraph compatibility
    private String serializer = Serializers.GRAPHBINARY_V1.toString();
    private int maxContentLength = 0;
}
