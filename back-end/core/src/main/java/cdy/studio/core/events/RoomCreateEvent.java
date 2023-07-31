package cdy.studio.core.events;

import cdy.studio.core.models.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomCreateEvent {
    private Room room;
}