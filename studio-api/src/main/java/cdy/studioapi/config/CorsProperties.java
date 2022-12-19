package cdy.studioapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String[] allowedHeaders;
    private String[] allowedOrigins;
    private String[] allowedMethods;
}
