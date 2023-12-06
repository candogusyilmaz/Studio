package dev.canverse.studio.api.features.authorization.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    @Column(nullable = false)
    private int level;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> rolePermissions;

    public Role(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Set<RolePermission> getRolePermissions() {
        return Hibernate.isInitialized(rolePermissions) ? rolePermissions : Set.of();
    }
}
