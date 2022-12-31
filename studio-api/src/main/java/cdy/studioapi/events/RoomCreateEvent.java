package cdy.studioapi.events;

import cdy.studioapi.models.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomCreateEvent {
    private Room room;
}
