package cdy.studioapi.views;

import cdy.studioapi.models.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserBasicView implements Serializable {
    private final int id;
    private final String username;
    private final String displayName;
    private final String email;

    public UserBasicView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
    }
}
