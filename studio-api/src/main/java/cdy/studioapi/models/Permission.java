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
    @Column(nullable = false, unique = true)
    private String displayName;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    protected Permission() {

    }

    public Permission(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public void updateDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
