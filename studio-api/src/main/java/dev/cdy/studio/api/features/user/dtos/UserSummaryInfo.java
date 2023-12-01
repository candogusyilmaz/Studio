package dev.cdy.studio.api.features.user.dtos;

import dev.cdy.studio.api.features.user.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryInfo implements Serializable {
    @Transient
    private int id;
    @Transient
    private String username;
    private String displayName;
    private String title;
    @Transient
    private String email;

    public UserSummaryInfo(User user) {
        if (user == null) return;

        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.title = user.getTitle();
        this.email = user.getEmail();
    }
}
