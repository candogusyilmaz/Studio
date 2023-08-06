package cdy.studio.service.config;

import cdy.studio.service.UserService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtManager implements JwtDecoder {
    private final NimbusJwtDecoder nimbusJwtDecoder;
    private final UserService userService;
    private final RsaKeyProperties rsaKeys;

    public JwtManager(UserService userService, RsaKeyProperties rsaKeys) {
        this.userService = userService;
        this.rsaKeys = rsaKeys;
        nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }

    @Override
    public Jwt decode(String token) {
        var decoded = nimbusJwtDecoder.decode(token);
        var username = decoded.getClaimAsString("sub");

        var tokenVersion = decoded.getClaimAsString("tokenVersion");

        if (tokenVersion != null) {
            var tokenVersionInDb = userService.getTokenVersion(username);

            if (!Objects.equals(Integer.valueOf(tokenVersion), tokenVersionInDb)) {
                throw new BadJwtException("Token version mismatch!");
            }
        }

        return decoded;
    }

    @Bean
    public JwtEncoder encoder() {
        var jwk = new RSAKey.Builder(rsaKeys.getPublicKey()).privateKey(rsaKeys.getPrivateKey()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }
}
