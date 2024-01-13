package dev.canverse.studio.api.features.slot.entities;

import dev.canverse.studio.api.features.reservation.entities.Reservation;
import dev.canverse.studio.api.features.room.entities.Room;
import dev.canverse.studio.api.features.user.entities.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "slots")
@SoftDelete(strategy = SoftDeleteType.DELETED)
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

    @CreatedBy
    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private User createdBy;

    @CreationTimestamp
    @JoinColumn(name = "created_at", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne
    @Setter(AccessLevel.NONE)
    private User updatedBy;

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
