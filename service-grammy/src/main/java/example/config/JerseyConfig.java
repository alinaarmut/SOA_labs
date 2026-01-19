package example.config;

import example.GrammyResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(GrammyResource.class);
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
}
    }