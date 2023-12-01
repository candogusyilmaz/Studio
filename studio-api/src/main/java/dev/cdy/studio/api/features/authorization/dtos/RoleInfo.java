package dev.cdy.studio.api.features.authorization.dtos;

import dev.cdy.studio.api.features.authorization.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleInfo implements Serializable {
    private int id;
    private String name;
    private List<PermissionInfo> permissions;

    public RoleInfo(Role role) {
        if (role == null) return;

        this.id = role.getId();
        this.name = role.getName();
        this.permissions = role.getRolePermissions().stream().map(s -> new PermissionInfo(s.getPermission())).toList();
    }
}