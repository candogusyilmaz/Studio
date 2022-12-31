package cdy.studioapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

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

    @ManyToOne(optional = false)
    private Location location;

    @Column(nullable = false)
    private boolean deleted;
}
