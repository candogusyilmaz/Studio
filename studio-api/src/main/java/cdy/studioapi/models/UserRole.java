package cdy.studioapi.models;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity {
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Role role;

    protected UserRole() {
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
