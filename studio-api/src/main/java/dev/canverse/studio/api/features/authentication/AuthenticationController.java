package dev.canverse.studio.api.features.authentication;

import dev.canverse.studio.api.features.authentication.dtos.CreateToken;
import dev.canverse.studio.api.features.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final TokenService tokenService;
    private final AuthenticationManager authManager;

    @PostMapping("/token")
    public ResponseEntity<CreateToken.Response> token(@RequestBody CreateToken.Request login) {
        var token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        var auth = authManager.authenticate(token);
        var user = (User) auth.getPrincipal();

        var body = tokenService.createAccessToken(user);
        var cookie = tokenService.createRefreshTokenCookie(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .body(body);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<CreateToken.Response> refreshToken(@CookieValue(name = "refresh-token") Optional<String> refreshToken) {
        if (refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var user = tokenService.getSecurityUser(refreshToken.get());
        var body = tokenService.createAccessToken(user);
        var cookie = tokenService.createRefreshTokenCookie(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .body(body);
    }
}