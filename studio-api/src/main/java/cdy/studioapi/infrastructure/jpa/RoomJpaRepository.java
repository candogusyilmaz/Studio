package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomJpaRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
}