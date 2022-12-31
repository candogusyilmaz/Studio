package cdy.studioapi.views;

import cdy.studioapi.models.Room;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RoomView implements Serializable {
    private final int id;
    private final String name;

    public RoomView(Room room) {
        this.id = room.getId();
        this.name = room.getName();
    }
}