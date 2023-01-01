package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Reservation;
import cdy.studioapi.views.ReservationView;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {

    @Modifying
    @Query("update Reservation set lastAction.id = :reservationActionId where id = :reservationId")
    int updateLastAction(int reservationId, int reservationActionId);

    @Query("select (count(r) = 0) from Reservation r " +
            "where r.slot.id = :slotId and (r.startDate <= :endDate and r.endDate >= :startDate)")
    boolean slotReservableBetweenDates(@NonNull int slotId, @NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate);

    @Query("select (count(r) > 0) from Reservation r " +
            "where r.user.id = :userId and " +
            "r.startDate <= :endDate and r.endDate >= :startDate and " +
            "r.lastAction.status = cdy.studioapi.enums.ReservationStatus.ACTIVE")
    boolean hasActiveReservationBetweenDates(@NonNull int userId, @NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate);

    @Query("select r from Reservation r ")
    @EntityGraph(attributePaths = {"user", "slot.items", "slot.room", "lastAction"})
    List<ReservationView> findAllAsReservationView();
}