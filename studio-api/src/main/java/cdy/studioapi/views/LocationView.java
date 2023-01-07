package cdy.studioapi.views;

import cdy.studioapi.models.Location;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;

@Getter
public class LocationView implements Serializable {
    private final int id;
    private final String name;
    private LocationParentView parent;
    private List<RoomView> rooms;

    public LocationView(Location location) {
        this.id = location.getId();
        this.name = location.getName();

        if(Hibernate.isInitialized(location.getParent())){
            this.parent = new LocationParentView(location.getParent());
        }

        if (Hibernate.isInitialized(location.getRooms())) {
            this.rooms = location.getRooms().stream().map(RoomView::new).toList();
        }
    }
}
