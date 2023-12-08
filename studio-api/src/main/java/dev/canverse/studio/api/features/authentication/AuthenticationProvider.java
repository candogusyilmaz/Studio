package dev.canverse.studio.api.features.authentication;

import dev.canverse.studio.api.features.user.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;

@Service
public class AuthenticationProvider {
    /**
     * Retrieves the authenticated user.
     *
     * @return A User object representing the authenticated user.
     */
    public User getAuthentication() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Gets the highest level of the authenticated user based on their roles.
     *
     * @return The highest level among the user's roles or Integer.MIN_VALUE if no roles are present.
     */
    public int getHighestLevel() {
        if (this.getAuthentication().getUserRoles().isEmpty())
            return Integer.MIN_VALUE;

        return Collections.max(this.getAuthentication().getUserRoles(), Comparator.comparing(s -> s.getRole().getLevel())).getRole().getLevel();
    }
}
