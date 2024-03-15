package dev.canverse.studio.api.features.authorization.entities;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role implements Serializable {
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

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeSave() {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return;

        Expect.of(level).lessThan(AuthenticationProvider.getHighestLevel(), "You cannot create/update/remove a role higher or equal to your level.");
    }

    public Set<RolePermission> getRolePermissions() {
        return Hibernate.isInitialized(rolePermissions) ? rolePermissions : Set.of();
    }
}
