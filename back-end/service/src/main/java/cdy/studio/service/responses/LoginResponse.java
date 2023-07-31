package cdy.studio.service.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String displayName;
    private String title;
    private String timezone;
    private String token;

    private List<String> permissions;
}
