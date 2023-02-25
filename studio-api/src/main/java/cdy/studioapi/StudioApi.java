package cdy.studioapi;

import cdy.studioapi.config.CorsProperties;
import cdy.studioapi.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class})
public class StudioApi {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(StudioApi.class, args);
    }
}
