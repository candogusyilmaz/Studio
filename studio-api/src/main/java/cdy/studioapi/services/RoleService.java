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

    public void create(RoleCreateRequest req) {
        if (roleRepository.existsByName(req.getName()))
            throw new BadRequestException("Bu isimde bir rol zaten var.");

        var role = new Role(req.getName(), req.getLevel());
        roleRepository.save(role);
    }
}
