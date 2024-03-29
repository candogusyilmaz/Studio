package dev.canverse.studio.api;

import dev.canverse.studio.api.features.authentication.jwt.JwtProperties;
import dev.canverse.studio.api.security.CorsProperties;
import dev.canverse.studio.api.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class, JwtProperties.class})
public class StudioApi {
    public static void main(String[] args) {
        SpringApplication.run(StudioApi.class, args);
    }
}
