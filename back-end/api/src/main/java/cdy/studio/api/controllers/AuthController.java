package cdy.studio.api.controllers;

import cdy.studio.core.models.User;
import cdy.studio.service.TokenService;
import cdy.studio.service.requests.LoginRequest;
import cdy.studio.service.responses.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authManager;

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> token(@RequestBody LoginRequest login) {
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
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "refresh-token") Optional<String> refreshToken) {
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