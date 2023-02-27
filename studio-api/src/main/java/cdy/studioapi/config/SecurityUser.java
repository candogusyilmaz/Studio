package cdy.studioapi.config;

import cdy.studioapi.views.UserView;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    @Getter
    private Integer id;
    private String username;
    private String password;
    @Getter
    private String displayName;
    @Getter
    private String title;
    @Getter
    private String email;
    private boolean isEnabled;
    @Getter
    private Integer tokenVersion;
    @Getter
    private String timezone;
    private List<SimpleGrantedAuthority> authorities;

    public SecurityUser(Integer id, String username, String email, String timezone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.timezone = timezone;
    }

    public SecurityUser(UserView user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.tokenVersion = user.getTokenVersion();
        this.displayName = user.getDisplayName();
        this.title = user.getTitle();
        this.email = user.getEmail();
        this.timezone = user.getTimezone();

        authorities = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .toList();
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
