package dev.cdy.studio.api.features.location.dtos;

import dev.cdy.studio.api.features.location.entities.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LocationInfo implements Serializable {
    private int id;
    private String name;
    private LocationParentInfo parent;

    public LocationInfo(Location location) {
        if (location == null)
            return;

        this.id = location.getId();
        this.name = location.getName();
        this.parent = new LocationParentInfo(location.getParent());
    }
}
