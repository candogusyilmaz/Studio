package cdy.studio.api;

import cdy.studio.service.config.CorsProperties;
import cdy.studio.service.config.JwtProperties;
import cdy.studio.service.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class, JwtProperties.class})
@EnableJpaRepositories(basePackages = {"cdy.studio.infrastructure"})
@ComponentScan(basePackages = {"cdy.studio"})
@EntityScan(basePackages = {"cdy.studio.core"})
public class Main {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Main.class, args);
    }
}
