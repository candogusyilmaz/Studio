package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "slot_items")
@Where(clause = "deleted = false")
public class SlotItem extends AuditableEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Slot slot;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item item;

    @Column(nullable = false)
    private boolean deleted;
}
