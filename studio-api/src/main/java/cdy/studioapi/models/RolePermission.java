package cdy.studioapi.models;

import lombok.Getter;

import jakarta.persistence.Entity;
        import jakarta.persistence.ManyToOne;
        import jakarta.persistence.Table;

@Getter
@Entity
@Table(name = "role_permissions")
public class RolePermission extends BaseEntity {
    @ManyToOne(optional = false)
    private Role role;
    @ManyToOne(optional = false)
    private Permission permission;

    protected RolePermission() {
    }

    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }
}
