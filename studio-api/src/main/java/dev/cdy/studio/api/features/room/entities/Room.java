package dev.cdy.studio.api.features.room.entities;

import dev.cdy.studio.api.features.location.entities.Location;
import dev.cdy.studio.api.features.room.events.RoomCreatedEvent;
import dev.cdy.studio.api.features.slot.entities.Slot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@Where(clause = "deleted = false")
// TODO: @SoftDelete annotation is available in the next version of Hibernate. Refactor this after spring has upgraded to the next version.
@NoArgsConstructor
public class Room extends AbstractAggregateRoot<Room> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Location location;

    @Column(nullable = false)
    private boolean deleted;

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
