package dev.canverse.studio.api.features.user.entities;

import dev.canverse.studio.api.features.shared.TimePeriod;
import dev.canverse.studio.api.features.shared.Timestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Setter
@Entity
@Table(name = "user_groups")
@NoArgsConstructor
@SoftDelete(strategy = SoftDeleteType.DELETED)
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private TimePeriod timePeriod;

    private Timestamp timestamp;

    private boolean disabled = false;
}
