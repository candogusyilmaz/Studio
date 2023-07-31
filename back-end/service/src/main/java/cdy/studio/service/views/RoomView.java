package cdy.studio.service.views;

import cdy.studio.core.models.Room;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;

@Getter
public class RoomView implements Serializable {
    private final int id;
    private final String name;
    private final int capacity;
    private LocationView location;
    private List<SlotView> slots;

    public RoomView(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.capacity = room.getCapacity();

        if (Hibernate.isInitialized(room.getLocation())) {
            this.location = new LocationView(room.getLocation());
        }

        if (Hibernate.isInitialized(room.getSlots())) {
            this.slots = room.getSlots().stream().map(SlotView::new).toList();
        }
    }
}