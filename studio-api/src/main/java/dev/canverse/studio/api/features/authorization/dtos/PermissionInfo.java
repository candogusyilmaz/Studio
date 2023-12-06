package dev.canverse.studio.api.features.authorization.dtos;

import dev.canverse.studio.api.features.authorization.entities.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PermissionInfo implements Serializable {
    private int id;
    private String name;
    private String displayName;

    public PermissionInfo(Permission per) {
        if (per == null) return;

        this.id = per.getId();
        this.name = per.getName();
        this.displayName = per.getDisplayName();
    }
}