package cdy.studioapi.services;

import cdy.studioapi.enums.ExceptionCode;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.RoleRepository;
import cdy.studioapi.infrastructure.UserRoleRepository;
import cdy.studioapi.models.Role;
import cdy.studioapi.models.User;
import cdy.studioapi.models.UserRole;
import cdy.studioapi.requests.RoleCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public Role getById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.ROLE_NOT_FOUND, "There are no records with the id " + id));
    }

    public Role createRole(RoleCreateRequest req) {
        if (roleRepository.exists(req.getName())) {
            throw new BadRequestException(ExceptionCode.ROLE_ALREADY_EXISTS, "Role already exists");
        }

        return roleRepository.save(req.asEntity());
    }

    public void assignRole(User user, Role role) {
        var exists = userRoleRepository.exists(user.getId(), role.getId());

        if (exists) {
            throw new BadRequestException(ExceptionCode.USER_HAS_ROLE, "Role is already assigned to the user.");
        }

        var roleMember = new UserRole(user, role);

        userRoleRepository.save(roleMember);
    }

    public void unassignRole(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }
}
