package cdy.studio.core.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "slots")
@Where(clause = "deleted = false")
public class Slot extends AuditableEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "slot")
    private Set<SlotItem> slotItems;

    @OneToMany(mappedBy = "slot")
    private Set<Reservation> reservations;

    @Column(nullable = false)
    private boolean deleted;
}
