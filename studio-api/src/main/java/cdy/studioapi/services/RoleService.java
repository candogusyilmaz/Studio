package cdy.studioapi.services;

import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.infrastructure.RoleRepository;
import cdy.studioapi.models.Role;
import cdy.studioapi.requests.RoleCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createRole(RoleCreateRequest req) {
        if (roleRepository.exists(req.getName())) {
            throw new BadRequestException("Role already exists");
        }

        roleRepository.save(Role.create(req.getName()));
    }
}
