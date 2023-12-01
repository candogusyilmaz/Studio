package dev.cdy.studio.api.features.room.repositories;

import dev.cdy.studio.api.features.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
}