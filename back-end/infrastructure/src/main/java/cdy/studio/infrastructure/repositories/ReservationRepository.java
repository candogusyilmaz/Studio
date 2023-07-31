package cdy.studio.infrastructure.repositories;

import cdy.studio.core.models.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    @Modifying
    @Query("update Reservation set lastAction.id = :reservationActionId where id = :reservationId")
    int updateLastAction(int reservationId, int reservationActionId);

    @Query("select r from Reservation r ")
    @EntityGraph(attributePaths = {"user", "slot.slotItems.item", "slot.room", "lastAction"})
    Set<Reservation> findAllAsReservationView();

    @Query("select r from Reservation r where r.user.id = :userId")
    @EntityGraph(attributePaths = {"user", "slot.slotItems.item", "slot.room.location", "lastAction"})
    Page<Reservation> findAllAsReservationView(int userId, Pageable page);

    @Query("select r from Reservation r where r.id = :id")
    @EntityGraph(attributePaths = {"slot", "user", "lastAction"})
    Optional<Reservation> findById(int id);
}