package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
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
        return "Reservation{" +
                "user=" + user.getId() +
                ", slot=" + slot.getId() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", lastAction=" + (lastAction == null ? "null" : lastAction.getId()) +
                '}';
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate.withSecond(59).truncatedTo(ChronoUnit.SECONDS);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate.withSecond(1).truncatedTo(ChronoUnit.SECONDS);
    }
}
