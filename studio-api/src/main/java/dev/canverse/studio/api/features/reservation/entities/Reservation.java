package dev.canverse.studio.api.features.reservation.entities;

import dev.canverse.studio.api.features.reservation.events.ReservationCreated;
import dev.canverse.studio.api.features.reservation.events.ReservationUpdated;
import dev.canverse.studio.api.features.shared.TimePeriod;
import dev.canverse.studio.api.features.slot.entities.Slot;
import dev.canverse.studio.api.features.user.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.domain.AbstractAggregateRoot;

@Getter
@Setter
@Entity
@Table(name = "reservations")
@NoArgsConstructor
public class Reservation extends AbstractAggregateRoot<Reservation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    private TimePeriod timePeriod;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinFormula("(SELECT ra.id FROM reservation_actions ra WHERE ra.reservation_id = id ORDER BY ra.id DESC LIMIT 1)")
    private ReservationAction lastAction;

    @PrePersist
    private void prePersist() {
        this.registerEvent(new ReservationCreated(this));
    }

    @PreUpdate
    private void preUpdate() {
        this.registerEvent(new ReservationUpdated(this));
    }
}
