package dev.canverse.studio.api.features.room.entities;

import dev.canverse.studio.api.features.location.entities.Location;
import dev.canverse.studio.api.features.room.events.RoomCreatedEvent;
import dev.canverse.studio.api.features.slot.entities.Slot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@SoftDelete(strategy = SoftDeleteType.DELETED)
@NoArgsConstructor
public class Room extends AbstractAggregateRoot<Room> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne(optional = false)
    private Location location;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Slot> slots;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.registerEvent(new RoomCreatedEvent(this));
    }

    public Set<Slot> getSlots() {
        return Hibernate.isInitialized(slots) ? slots : Set.of();
    }
}
