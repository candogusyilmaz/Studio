package cdy.studioapi.views;

import cdy.studioapi.models.Role;
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

        if(Hibernate.isInitialized(role.getPermissions())) {
            this.permissions = role.getPermissions().stream().map(PermissionView::new).toList();
        }
    }
}