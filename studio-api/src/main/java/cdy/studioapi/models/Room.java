package cdy.studioapi.models;

import cdy.studioapi.models.listeners.RoomListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@EntityListeners(RoomListener.class)
@Table(name = "rooms")
@Where(clause = "deleted = false")
public class Room extends AuditableEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne(optional = false)
    private Location location;

    @Column(nullable = false)
    private boolean deleted;
}
