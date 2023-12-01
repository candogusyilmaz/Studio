package dev.cdy.studio.api.features.reservation.repositories;

import dev.cdy.studio.api.features.reservation.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    @Modifying
    @Query("update Reservation set lastAction.id = :reservationActionId where id = :reservationId")
    void updateLastAction(int reservationId, int reservationActionId);

    @Query("select r from Reservation r where r.user.id = :userId")
    @EntityGraph(attributePaths = {"user", "slot.room.location", "lastAction"})
    Page<Reservation> findByUserId(int userId, Pageable page);

    @Query("select r from Reservation r where r.id = :reservationId and r.user.id = :userId")
    @EntityGraph(attributePaths = {"slot", "user", "lastAction"})
    Optional<Reservation> findReservationByUserId(int userId, int reservationId);

    default boolean conflictingWithSelf(int reservationId, int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate).and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return this.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    default boolean conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate);

        return this.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    default boolean conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate);

        return this.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    default boolean conflictingWithOthers(int reservationId, int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate).and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return this.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }
}