package cdy.studioapi.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "slot_items")
@Where(clause = "deleted = false")
public class SlotItem extends AuditableEntity {
    @ManyToOne(optional = false)
    private Slot slot;

    @ManyToOne(optional = false)
    private Item item;

    @Column(nullable = false)
    private boolean deleted;
}
