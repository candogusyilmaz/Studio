package cdy.studioapi.views;

import cdy.studioapi.models.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SimpleUserView implements Serializable {
    private final int id;
    private final String username;
    private final String email;

    public SimpleUserView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
