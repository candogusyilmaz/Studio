package cdy.studioapi.services;

import cdy.studioapi.dtos.RoleCreateDto;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.jpa.RoleJpaRepository;
import cdy.studioapi.infrastructure.jpa.UserRoleJpaRepository;
import cdy.studioapi.models.Role;
import cdy.studioapi.models.User;
import cdy.studioapi.models.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleJpaRepository roleRepository;
    private final UserRoleJpaRepository userRoleRepository;

    public Role getById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There are no records with the id " + id));
    }

    public Role createRole(RoleCreateDto req) {
        if (roleRepository.exists(req.getName())) {
            throw new BadRequestException("Role already exists");
        }

        return roleRepository.save(req.asEntity());
    }

    public void assignRole(User user, Role role) {
        var exists = userRoleRepository.exists(user.getId(), role.getId());

        if (exists) {
            throw new BadRequestException("Role is already assigned to the user.");
        }

        var roleMember = new UserRole(user, role);

        userRoleRepository.save(roleMember);
    }

    public void unassignRole(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }
}
