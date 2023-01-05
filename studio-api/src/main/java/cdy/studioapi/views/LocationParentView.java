package cdy.studioapi.views;

import cdy.studioapi.models.Location;
import lombok.Getter;

@Getter
public class LocationParentView {
    private int id;
    private String name;

    public LocationParentView(Location location) {
        this.id = location.getId();
        this.name = location.getName();
    }
}
