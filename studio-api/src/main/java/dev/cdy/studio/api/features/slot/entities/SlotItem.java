package dev.cdy.studio.api.features.slot.entities;

import dev.cdy.studio.api.features.item.entities.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "slot_items")
@Where(clause = "deleted = false")
// TODO: @SoftDelete annotation is available in the next version of Hibernate. Refactor this after spring has upgraded to the next version.
@NoArgsConstructor
public class SlotItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Slot slot;

    @ManyToOne(optional = false)
    private Item item;

    @Column(nullable = false)
    private boolean deleted;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;
}
