package com.microsoft.spring.data.gremlin.autoconfigure;

import com.microsoft.spring.data.gremlin.common.GremlinConfig;
import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.config.GremlinConfigurationSupport;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.query.GremlinOperations;
import com.microsoft.spring.data.gremlin.query.GremlinTemplate;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration that creates a GremlinOperations bean (GremlinTemplate)
 * when gremlin client classes are on the classpath.
 *
 * This eases using this library with JanusGraph Gremlin Server by providing
 * sensible defaults (localhost:8182, GraphBinary serializer, no SSL).
 */
@AutoConfiguration
@ConditionalOnClass(GremlinTemplate.class)
@EnableConfigurationProperties(GremlinProperties.class)
public class GremlinAutoConfiguration extends GremlinConfigurationSupport {

    private final GremlinProperties properties;
    
    @Autowired(required = false)
    private TypeSerializerRegistry typeSerializerRegistry;

    public GremlinAutoConfiguration(GremlinProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public GremlinConfig gremlinConfig() {
        return GremlinConfig.builder()
                .endpoint(properties.getEndpoint())
                .port(properties.getPort())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .sslEnabled(properties.isSslEnabled())
                .telemetryAllowed(properties.isTelemetryAllowed())
                .serializer(properties.getSerializer())
                .maxContentLength(properties.getMaxContentLength())
                .typeSerializerRegistry(typeSerializerRegistry)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GremlinFactory gremlinFactory(GremlinConfig gremlinConfig) {
        return new GremlinFactory(gremlinConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingGremlinConverter mappingGremlinConverter() throws ClassNotFoundException {
        return new MappingGremlinConverter(gremlinMappingContext());
    }

    @Bean
    @ConditionalOnMissingBean({GremlinOperations.class, GremlinTemplate.class})
    public GremlinTemplate gremlinTemplate(GremlinFactory factory, MappingGremlinConverter converter,
                                           ApplicationContext context) throws ClassNotFoundException {
        GremlinTemplate template = new GremlinTemplate(factory, converter);
        template.setApplicationContext(context);
        return template;
    }
}
