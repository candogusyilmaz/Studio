package dev.cdy.studio.api.features.authentication.dtos;

import dev.cdy.studio.api.features.user.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class CreateToken {
    private CreateToken() {
    }

    @Getter
    @Setter
    public static class Request {
        private String username;
        private String password;
    }

    @Getter
    public static class Response {
        private final String displayName;
        private final String title;
        private final String timezone;
        private final String token;
        private final List<String> permissions;

        public Response(User user, String token) {
            this.displayName = user.getDisplayName();
            this.title = user.getTitle();
            this.timezone = user.getTimezone();
            this.token = token;
            this.permissions = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        }
    }
}
