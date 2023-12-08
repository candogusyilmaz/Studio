package dev.canverse.studio.api.features.authentication;

import dev.canverse.studio.api.features.authentication.dtos.CreateToken;
import dev.canverse.studio.api.features.authentication.jwt.JwtProperties;
import dev.canverse.studio.api.features.user.entities.User;
import dev.canverse.studio.api.features.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    /**
     * Retrieves a security user based on the provided refresh token.
     *
     * @param refreshToken The refresh token used for decoding and extracting user information.
     * @return A User object representing the security user.
     * @throws IllegalArgumentException Thrown if there is an issue decoding the refresh token or if the user is not found.
     */
    public User getSecurityUser(String refreshToken) {
        var jwt = jwtDecoder.decode(refreshToken);
        var username = jwt.getClaimAsString("sub");
        return (User) userService.loadUserByUsername(username);
    }

    /**
     * Creates a refresh token cookie for the provided security user.
     *
     * @param principal The security user for whom the refresh token cookie is created.
     * @return A string representation of the refresh token cookie.
     */
    public String createRefreshTokenCookie(User principal) {
        var now = Instant.now();
        var expiresAt = now.plus(jwtProperties.getRefreshTokenExpirationInSeconds(), ChronoUnit.SECONDS);

        var claims = JwtClaimsSet.builder()
                .issuer("studio-api")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(principal.getUsername())
                .claim("tokenVersion", principal.getTokenVersion())
                .build();

        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseCookie.from("refresh-token", token)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenExpirationInSeconds())
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .build()
                .toString();
    }

    /**
     * Creates an access token for the provided security user.
     *
     * @param principal The security user for whom the access token is created.
     * @return A CreateToken.Response object containing the security user and the access token.
     */
    public CreateToken.Response createAccessToken(User principal) {
        var now = Instant.now();
        var expiresAt = now.plus(jwtProperties.getAccessTokenExpirationInSeconds(), ChronoUnit.SECONDS);

        var claims = JwtClaimsSet.builder()
                .issuer("studio-api")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(principal.getUsername())
                .claim("timezone", principal.getTimezone())
                .build();

        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new CreateToken.Response(principal, token);
    }
}
