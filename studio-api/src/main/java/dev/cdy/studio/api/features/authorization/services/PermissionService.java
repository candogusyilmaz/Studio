package dev.cdy.studio.api.features.authorization.services;

import dev.cdy.studio.api.exceptions.NotFoundException;
import dev.cdy.studio.api.features.authorization.dtos.PermissionInfo;
import dev.cdy.studio.api.features.authorization.repositories.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Page<PermissionInfo> getAll(Pageable page) {
        return permissionRepository.findBy((root, query, criteriaBuilder) -> null, r -> r.sortBy(page.getSort()).page(page).map(PermissionInfo::new));
    }

    public PermissionInfo getById(int id) {
        return permissionRepository.findById(id).map(PermissionInfo::new)
                .orElseThrow(() -> new NotFoundException("Id: %s ile eşleşen yetki bulunumadı.".formatted(id)));
    }

    public void updateDisplayName(int id, String displayName) {
        var permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id: %s ile eşleşen yetki bulunumadı.".formatted(id)));

        permission.setDisplayName(displayName);
        permissionRepository.saveAndFlush(permission);
    }
}
