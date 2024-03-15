package dev.canverse.studio.api.features.reservation.repositories;

import dev.canverse.studio.api.features.reservation.entities.Reservation;
import org.springframework.data.jpa.domain.Specification;

public class ReservationSpecifications {
    private ReservationSpecifications() {
    }

    public static Specification<Reservation> findByReservationIdAndUserId(int userId, int reservationId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), reservationId),
                cb.equal(root.get("user").get("id"), userId)
        );
    }
}
