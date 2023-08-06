package cdy.studio.service.views;

import cdy.studio.core.models.Location;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LocationParentlessView implements Serializable {
    private final int id;
    private final String name;

    public LocationParentlessView(Location location) {
        this.id = location.getId();
        this.name = location.getName();
    }
}
