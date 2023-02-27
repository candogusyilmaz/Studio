package cdy.studioapi.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private String displayName;
    private String title;
    private String timezone;
    private String accessToken;
    private LocalDateTime expiresAt;

    private List<String> permissions;
}
