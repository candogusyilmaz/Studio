package dev.cdy.studio.api.features.location.dtos;

import dev.cdy.studio.api.features.location.entities.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LocationParentInfo implements Serializable {
    private int id;
    private String name;

    public LocationParentInfo(Location location) {
        if (location == null)
            return;

        this.id = location.getId();
        this.name = location.getName();
    }
}
