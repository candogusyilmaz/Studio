package cdy.studioapi.infrastructure.specs;

import cdy.studioapi.models.Slot;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SlotSpecifications {
    private SlotSpecifications() {
    }

    public static Specification<Slot> availableSlots(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            var res = root.join("reservations", JoinType.LEFT);

            res.on(
                    cb.and(
                            cb.lessThanOrEqualTo(res.get("startDate"), endDate),
                            cb.greaterThanOrEqualTo(res.get("endDate"), startDate)
                    )
            );

            return cb.and(
                    cb.isNull(res.get("slot").get("id"))
            );
        };
    }

    public static Specification<Slot> roomIdEquals(int roomId) {
        return (root, query, cb) -> {
            var room = root.join("room", JoinType.LEFT);

            return cb.equal(room.get("id"), roomId);
        };
    }
}
