package example.springdata.gremlin.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Example configuration showing how to provide a custom TypeSerializerRegistry bean.
 * This overrides the auto-configured TypeSerializerRegistry and provides
 * JanusGraph serialization support.
 */
@Configuration
@Slf4j
public class CustomTypeSerializerConfiguration {

    @Bean
    public TypeSerializerRegistry typeSerializerRegistry() {
        log.info("Creating custom TypeSerializerRegistry with JanusGraph IoRegistry support");
        
        // Create JanusGraph IoRegistry
        JanusGraphIoRegistry janusRegistry = JanusGraphIoRegistry.instance();
        
        // Build TypeSerializerRegistry with JanusGraph support
        TypeSerializerRegistry registry = TypeSerializerRegistry.build()
                .addRegistry(janusRegistry)
                .create();
                
        log.info("Successfully configured custom TypeSerializerRegistry with JanusGraph support");
        return registry;
    }
}
