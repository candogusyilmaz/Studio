package dev.canverse.studio.api.features.authentication.jwt;

import com.google.common.base.Preconditions;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import dev.canverse.studio.api.features.user.services.UserService;
import dev.canverse.studio.api.security.RsaKeyProperties;
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

        var tokenVersionString = decoded.getClaimAsString("tokenVersion");

        if (tokenVersionString != null) {
            var tokenVersion = Integer.valueOf(tokenVersionString);
            var tokenVersionInDb = userService.getTokenVersion(username);

            Preconditions.checkArgument(!Objects.equals(tokenVersion, tokenVersionInDb), "Token version mismatch!");
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
