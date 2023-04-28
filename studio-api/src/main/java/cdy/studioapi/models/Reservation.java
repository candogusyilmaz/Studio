package cdy.studioapi.models;

import cdy.studioapi.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Table(name = "reservations")
@NoArgsConstructor
public class Reservation extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_action_id")
    private ReservationAction lastAction;

    @Override
    public String toString() {
        return """
                {
                      "userId": %d,
                      "slotId": %d,
                      "startDate": "%s",
                      "endDate": "%s",
                      "lastActionId": %s
                }
                """.formatted(user.getId(), slot.getId(), startDate, endDate, lastAction == null ? "none" : lastAction.getId());
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate.withSecond(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate.withSecond(1).truncatedTo(ChronoUnit.SECONDS);
    }

    @PreUpdate
    @PrePersist
    private void upsertPreChecks() {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Başlangıç tarihi bitiş tarihinden sonra olmalıdır.");
        }

        if (Duration.between(startDate, endDate).toMinutes() < Duration.ofMinutes(10).toMinutes()) {
            throw new BadRequestException("Başlangıç tarihi ile bitiş tarihi arasında en az 10 dakika olmalıdır.");
        }
    }
}
