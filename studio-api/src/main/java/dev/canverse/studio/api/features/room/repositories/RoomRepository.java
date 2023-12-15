package dev.canverse.studio.api.features.room.repositories;

import dev.canverse.studio.api.features.room.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    @Query("SELECT COUNT(r) > 0 FROM Room r WHERE lower(r.name) = lower(:name)  AND r.location.id = :locationId")
    boolean existsByName(String name, int locationId);
}