package dev.canverse.studio.api.features.reservation.repositories;

import dev.canverse.studio.api.features.reservation.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    @Query("select r from Reservation r where r.user.id = :userId")
    @EntityGraph(attributePaths = {"user", "slot.room.location", "lastAction"})
    Page<Reservation> findByUserId(int userId, Pageable page);

    @Query("select r from Reservation r where r.id = :reservationId and r.user.id = :userId")
    @EntityGraph(attributePaths = {"slot", "user", "lastAction"})
    Optional<Reservation> findReservationByUserId(int userId, int reservationId);

    @Query("select count(r) > 0 from Reservation r " +
            "where r.id = :reservationId and r.user.id = :userId and r.timePeriod.startDate <= :endDate and r.timePeriod.endDate >= :startDate")
    boolean conflictingWithSelf(int reservationId, int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(r) > 0 from Reservation r " +
            "where r.user.id = :userId and r.timePeriod.startDate <= :endDate and r.timePeriod.endDate >= :startDate")
    boolean conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(r) > 0 from Reservation r " +
            "where r.slot.id = :slotId and r.timePeriod.startDate <= :endDate and r.timePeriod.endDate >= :startDate")
    boolean conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(r) > 0 from Reservation r " +
            "where r.id = :reservationId and r.slot.id = :slotId and r.timePeriod.startDate <= :endDate and r.timePeriod.endDate >= :startDate")
    boolean conflictingWithOthers(int reservationId, int slotId, LocalDateTime startDate, LocalDateTime endDate);
}