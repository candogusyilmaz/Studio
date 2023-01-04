package cdy.studioapi.services;

import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.infrastructure.jpa.UserRoleJpaRepository;
import cdy.studioapi.models.Role;
import cdy.studioapi.models.User;
import cdy.studioapi.models.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleService {
    private final UserRoleJpaRepository userRoleRepository;

    public UserRole assignRole(User user, Role role) {

        var exists = existsRoleMember(user, role);

        if (exists) {
            throw new BadRequestException("User already has the role.");
        }

        var roleMember = new UserRole(user, role);

        return userRoleRepository.save(roleMember);
    }

    public void unassignRole(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    public boolean existsRoleMember(User user, Role role) {
        return userRoleRepository.exists(user.getId(), role.getId());
    }
}
