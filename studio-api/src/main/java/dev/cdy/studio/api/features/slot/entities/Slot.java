package dev.cdy.studio.api.features.slot.entities;

import dev.cdy.studio.api.features.reservation.entities.Reservation;
import dev.cdy.studio.api.features.room.entities.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "slots")
@Where(clause = "deleted = false")
// TODO: @SoftDelete annotation is available in the next version of Hibernate. Refactor this after spring has upgraded to the next version.
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "slot")
    private Set<SlotItem> slotItems;

    @OneToMany(mappedBy = "slot")
    private Set<Reservation> reservations;

    @Column(nullable = false)
    private boolean deleted;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    public Set<SlotItem> getSlotItems() {
        return Hibernate.isInitialized(slotItems) ? slotItems : Set.of();
    }

    public Set<Reservation> getReservations() {
        return Hibernate.isInitialized(reservations) ? reservations : Set.of();
    }
}
