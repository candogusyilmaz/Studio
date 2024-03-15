package dev.canverse.studio.api.features.user.entities;

import dev.canverse.studio.api.features.authorization.entities.Role;
import dev.canverse.studio.api.features.shared.TimePeriod;
import dev.canverse.studio.api.features.shared.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@SQLRestriction("current_timestamp between start_date and end_date")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Role role;

    @Setter
    private TimePeriod timePeriod;

    private Timestamp timestamp;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
