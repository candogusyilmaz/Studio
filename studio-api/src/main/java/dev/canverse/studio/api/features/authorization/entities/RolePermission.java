package dev.canverse.studio.api.features.authorization.entities;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "role_permissions")
@NoArgsConstructor
public class RolePermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Role role;

    @ManyToOne(optional = false)
    private Permission permission;

    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeSave() {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            return;

        Expect.of(role.getLevel()).lessThan(AuthenticationProvider.getHighestLevel(), "You cannot modify a role's permissions higher or equal to your level.");
    }
}
