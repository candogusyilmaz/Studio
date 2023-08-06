package cdy.studio.service.views;

import cdy.studio.core.models.User;
import lombok.Getter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

@Getter
public class UserBasicView implements Serializable {
    @Transient
    private final int id;
    @Transient
    private final String username;
    private final String displayName;
    private final String title;
    @Transient
    private final String email;

    public UserBasicView(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.title = user.getTitle();
        this.email = user.getEmail();
    }
}
