package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Reservation;
import cdy.studioapi.views.ReservationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    @Modifying
    @Query("update Reservation set lastAction.id = :reservationActionId where id = :reservationId")
    int updateLastAction(int reservationId, int reservationActionId);

    @Query("select r from Reservation r ")
    @EntityGraph(attributePaths = {"user", "slot.items", "slot.room", "lastAction"})
    List<ReservationView> findAllAsReservationView();

    @Query("select r from Reservation r where r.user.id = :userId")
    @EntityGraph(attributePaths = {"user", "slot.items", "slot.room.location", "lastAction"})
    Page<ReservationView> findAllAsReservationView(int userId, Pageable page);

    @Query("select r from Reservation r where r.id = :id")
    @EntityGraph(attributePaths = {"slot", "user", "lastAction"})
    Optional<Reservation> findById(int id);
}