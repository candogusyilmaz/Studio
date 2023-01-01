package cdy.studioapi.models;


import cdy.studioapi.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation_actions")
public class ReservationAction extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private String description;

    @Column(nullable = false)
    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime actionDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User actionBy;
}
