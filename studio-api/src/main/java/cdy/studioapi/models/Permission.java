package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String displayName;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    protected Permission() {

    }

    public static Permission create(String name, String displayName) {
        var permission = new Permission();
        permission.name = name;
        permission.displayName = displayName;
        return permission;
    }
}
