package cdy.studioapi.services;

import cdy.studioapi.config.JwtProperties;
import cdy.studioapi.models.User;
import cdy.studioapi.responses.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    public User getSecurityUser(String refreshToken) {
        var jwt = jwtDecoder.decode(refreshToken);
        var username = jwt.getClaimAsString("sub");
        return (User) userService.loadUserByUsername(username);
    }

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

    public LoginResponse createAccessToken(User principal) {
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

        var permissions = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new LoginResponse(
                principal.getDisplayName(),
                principal.getTitle(),
                principal.getTimezone(),
                token,
                permissions);
    }
}
