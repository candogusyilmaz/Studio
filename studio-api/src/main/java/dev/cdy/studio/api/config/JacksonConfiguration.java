package dev.cdy.studio.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.findModulesViaServiceLoader(true);
        };
    }
}
