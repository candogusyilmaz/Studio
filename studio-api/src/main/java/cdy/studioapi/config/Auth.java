package cdy.studioapi.config;

import cdy.studioapi.models.User;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Objects;

@Getter
public class Auth extends UsernamePasswordAuthenticationToken {
    private final SecurityUser user;

    public Auth(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super(user, null, authorities);
        this.user = user;
    }

    public static User asUser() {
        var auth = (Auth) SecurityContextHolder.getContext().getAuthentication();

        return new User(auth.getUser().getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Auth auth = (Auth) o;
        return user.equals(auth.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
