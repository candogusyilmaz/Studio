package cdy.studioapi.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class Auth extends UsernamePasswordAuthenticationToken {
    private final SecurityUser user;

    public Auth(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super(user, null, authorities);
        this.user = user;
    }
}
