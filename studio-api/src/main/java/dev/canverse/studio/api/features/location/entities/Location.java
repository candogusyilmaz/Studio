package dev.canverse.studio.api.features.location.entities;

import dev.canverse.studio.api.features.room.entities.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "locations")
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Location parent;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;

    public Location(String name) {
        this.name = name;
    }

    public Set<Room> getRooms() {
        return Hibernate.isInitialized(rooms) ? rooms : Set.of();
    }
}
