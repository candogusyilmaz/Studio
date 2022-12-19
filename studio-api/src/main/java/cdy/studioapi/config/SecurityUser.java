package cdy.studioapi.config;

import cdy.studioapi.dtos.UserView;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
    @Getter
    private final Integer id;
    private final String username;
    private final String password;
    @Getter
    private final String displayName;
    @Getter
    private final String email;
    private final boolean isEnabled;
    @Getter
    private final Integer tokenVersion;
    private final List<GrantedAuthority> authorities;

    public SecurityUser(UserView user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isEnabled = user.getIsEnabled();
        this.tokenVersion = user.getTokenVersion();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();

        authorities = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
