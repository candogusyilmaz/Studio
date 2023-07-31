package cdy.studio.infrastructure.specifications;

import cdy.studio.core.enums.ReservationStatus;
import cdy.studio.core.models.Reservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReservationSpecifications {
    private ReservationSpecifications() {
    }

    public static Specification<Reservation> conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("slot").get("id"), slotId),
                cb.lessThanOrEqualTo(root.get("startDate"), endDate),
                cb.greaterThanOrEqualTo(root.get("endDate"), startDate),
                cb.isFalse(root.get("lastAction").get("status")
                        .in(ReservationStatus.COMPLETED,
                                ReservationStatus.REJECTED,
                                ReservationStatus.CANCELLED))
        );
    }

    public static Specification<Reservation> conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("user").get("id"), userId),
                cb.lessThanOrEqualTo(root.get("startDate"), endDate),
                cb.greaterThanOrEqualTo(root.get("endDate"), startDate),
                cb.isFalse(root.get("lastAction").get("status")
                        .in(ReservationStatus.COMPLETED,
                                ReservationStatus.REJECTED,
                                ReservationStatus.CANCELLED))
        );
    }

    public static Specification<Reservation> reservationIdNotEqual(int reservationId) {
        return (root, query, cb) -> cb.notEqual(root.get("id"), reservationId);
    }

    public static Specification<Reservation> getUserReservationById(int userId, int reservationId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), reservationId),
                cb.equal(root.get("user").get("id"), userId)
        );
    }
}
