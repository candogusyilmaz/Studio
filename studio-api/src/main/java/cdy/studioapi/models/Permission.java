package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@NoArgsConstructor
public class Permission extends BaseEntity implements GrantedAuthority {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String displayName;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions;

    public Permission(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
