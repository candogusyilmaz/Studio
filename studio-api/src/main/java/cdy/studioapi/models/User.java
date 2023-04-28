package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String displayName;
    private String title;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private Boolean isEnabled = false;
    private int tokenVersion;
    private String timezone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;

    public User(int id) {
        super(id);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isEnabled = true;
        this.tokenVersion = 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // get userRoles -> get roles -> get rolePermissions -> get permissions -> get permission names
        return this.getUserRoles().stream()
                .flatMap(ur -> ur.getRole().getRolePermissions().stream())
                .map(RolePermission::getPermission)
                .toList();
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
