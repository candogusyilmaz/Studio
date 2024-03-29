package dev.canverse.studio.api.features.room.events;

import dev.canverse.studio.api.features.room.entities.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomCreatedEvent {
    private Room room;
}
