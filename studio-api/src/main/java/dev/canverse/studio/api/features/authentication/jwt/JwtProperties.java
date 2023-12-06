package dev.canverse.studio.api.features.authentication.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private int accessTokenExpirationInSeconds;
    private int refreshTokenExpirationInSeconds;
}
