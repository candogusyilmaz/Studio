package dev.cdy.studio.api.features.authentication;

import dev.cdy.studio.api.features.user.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;

@Service
public class AuthenticationProvider {
    public User getAuthentication() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public int getHighestLevel() {
        if (this.getAuthentication().getUserRoles().isEmpty())
            return Integer.MIN_VALUE;

        return Collections.max(this.getAuthentication().getUserRoles(), Comparator.comparing(s -> s.getRole().getLevel())).getRole().getLevel();
    }
}
