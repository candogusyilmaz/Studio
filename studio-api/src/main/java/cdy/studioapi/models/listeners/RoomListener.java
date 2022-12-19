package cdy.studioapi.models.listeners;

import cdy.studioapi.models.Room;

import javax.persistence.PostPersist;

public class RoomListener {

    @PostPersist
    private void afterRoomCreated(Room room) {
        // TODO create the slots
    }
}
