package cdy.studioapi.views;

import cdy.studioapi.models.User;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class UserView implements Serializable {
    private final int id;
    private final String username;
    private final String password;
    private final String displayName;
    private final String email;
    private final boolean isEnabled;
    private final int tokenVersion;
    private final String timezone;

    private final List<RoleView> roles;

    public UserView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.isEnabled = user.getIsEnabled();
        this.tokenVersion = user.getTokenVersion();
        this.timezone = user.getTimezone();
        this.roles = user.getRoles().stream().map(RoleView::new).toList();
    }
}