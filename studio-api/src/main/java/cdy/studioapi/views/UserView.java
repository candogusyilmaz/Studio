package cdy.studioapi.views;

import cdy.studioapi.models.User;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;

@Getter
public class UserView implements Serializable {
    private final int id;
    private final String username;
    private final String displayName;
    private final String title;
    private final String email;
    private final boolean isEnabled;
    private final int tokenVersion;
    private final String timezone;
    private List<RoleView> roles;

    public UserView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.title = user.getTitle();
        this.email = user.getEmail();
        this.isEnabled = user.getIsEnabled();
        this.tokenVersion = user.getTokenVersion();
        this.timezone = user.getTimezone();

        if (Hibernate.isInitialized(user.getUserRoles())) {
            this.roles = user.getUserRoles().stream().map(s -> new RoleView(s.getRole())).toList();
        }
    }
}