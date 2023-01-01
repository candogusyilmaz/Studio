package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String displayName;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private Boolean isEnabled = false;
    private int tokenVersion;
    private String timezone;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    public User(int id) {
        super(id);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isEnabled = true;
        this.tokenVersion = 1;
    }
}
