package cdy.studioapi.views;

import cdy.studioapi.models.Location;
import lombok.Getter;

import java.util.List;

@Getter
public class LocationView {
    private int id;
    private String name;
    private LocationParentView parent;
    private List<RoomView> rooms;

    public LocationView(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.parent = new LocationParentView(location);
        this.rooms = location.getRooms().stream().map(RoomView::new).toList();
    }
}
