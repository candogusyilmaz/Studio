package cdy.studio.service.views;

import cdy.studio.core.models.Role;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;

@Getter
public class RoleView implements Serializable {
    private final int id;
    private final String name;
    private List<PermissionView> permissions;

    public RoleView(Role role) {
        this.id = role.getId();
        this.name = role.getName();

        if (Hibernate.isInitialized(role.getRolePermissions())) {
            this.permissions = role.getRolePermissions().stream().map(s -> new PermissionView(s.getPermission())).toList();
        }
    }
}