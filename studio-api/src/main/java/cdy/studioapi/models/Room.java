package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@Where(clause = "deleted = false")
public class Room extends AuditableEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Location location;

    @Column(nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "room")
    private List<Slot> slots;
}
