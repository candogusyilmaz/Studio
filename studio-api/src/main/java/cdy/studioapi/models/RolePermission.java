package cdy.studioapi.models;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
