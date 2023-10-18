package cdy.studio.core.models;

import cdy.studio.core.events.ReservationCreated;
import cdy.studio.core.events.ReservationUpdated;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Table(name = "reservations")
@NoArgsConstructor
public class Reservation extends AbstractAggregateRoot<Reservation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime startDate;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
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

    @PrePersist
    private void prePersist() {
        this.registerEvent(new ReservationCreated(this));
    }

    @PreUpdate
    private void preUpdate() {
        this.registerEvent(new ReservationUpdated(this));
    }

    public void setDate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate.truncatedTo(ChronoUnit.MINUTES);
        this.endDate = endDate.truncatedTo(ChronoUnit.MINUTES);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Başlangıç tarihi bitiş tarihinden sonra olmalıdır.");
        }

        if (Duration.between(startDate, endDate).toMinutes() < Duration.ofMinutes(10).toMinutes()) {
            throw new IllegalArgumentException("Başlangıç tarihi ile bitiş tarihi arasında en az 10 dakika olmalıdır.");
        }
    }
}
