package cdy.studioapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "locations")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Location extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Location parent;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Room> rooms;

    public Location(String name) {
        this.name = name;
    }
}
