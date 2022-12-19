package cdy.studioapi.services;

import cdy.studioapi.dtos.RoleDto;
import cdy.studioapi.infrastructure.RoleRepository;
import cdy.studioapi.models.Role;
import cdy.studioapi.requests.RoleCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Page<RoleDto> getAll(Pageable pageable) {
        return roleRepository.getAllPaged(pageable);
    }

    public Role getById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There are no records with the id " + id));
    }

    public Role createRole(RoleCreateRequest req) {
        if (roleRepository.exists(req.getName())) {
            throw new RuntimeException("Role already exists");
        }

        return roleRepository.save(req.asEntity());
    }
}
