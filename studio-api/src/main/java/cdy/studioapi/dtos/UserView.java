package cdy.studioapi.dtos;

import cdy.studioapi.models.Permission;
import cdy.studioapi.models.Role;
import cdy.studioapi.models.User;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import java.util.List;

@EntityView(User.class)
public interface UserView {

    @IdMapping
    int getId();

    String getUsername();

    String getPassword();

    String getDisplayName();

    String getEmail();

    boolean getIsEnabled();

    int getTokenVersion();

    String getTimezone();

    List<RoleView> getRoles();

    @EntityView(Role.class)
    interface RoleView {

        @IdMapping
        int getId();

        String getName();

        List<PermissionView> getPermissions();
    }

    @EntityView(Permission.class)
    interface PermissionView {

        @IdMapping
        int getId();

        String getName();

        String getDisplayName();
    }
}