package dev.cdy.studio.api.features.room.dtos;

import dev.cdy.studio.api.features.location.dtos.LocationInfo;
import dev.cdy.studio.api.features.room.entities.Room;
import dev.cdy.studio.api.features.slot.dtos.SlotInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RoomInfo implements Serializable {
    private int id;
    private String name;
    private int capacity;
    private LocationInfo location;
    private Set<SlotInfo> slots;

    public RoomInfo(Room room) {
        if (room == null) return;

        this.id = room.getId();
        this.name = room.getName();
        this.capacity = room.getCapacity();
        this.slots = room.getSlots().stream().map(SlotInfo::new).collect(Collectors.toSet());
    }
}