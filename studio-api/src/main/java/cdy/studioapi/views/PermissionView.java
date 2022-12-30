package cdy.studioapi.views;

import cdy.studioapi.models.Permission;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PermissionView implements Serializable {
    private final int id;
    private final String name;
    private final String displayName;

    public PermissionView(Permission per) {
        this.id = per.getId();
        this.name = per.getName();
        this.displayName = per.getDisplayName();
    }
}