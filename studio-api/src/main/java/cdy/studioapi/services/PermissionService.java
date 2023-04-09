package cdy.studioapi.services;

import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.PermissionRepository;
import cdy.studioapi.views.PermissionView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Page<PermissionView> getAll(Pageable page) {
        return permissionRepository.findBy((root, query, criteriaBuilder) -> null, r -> r.sortBy(page.getSort()).page(page).map(PermissionView::new));
    }

    public PermissionView getById(int id) {
        return permissionRepository.findById(id).map(PermissionView::new)
                .orElseThrow(() -> new NotFoundException("Id: %s ile eşleşen yetki bulunumadı.".formatted(id)));
    }

    public void updateDisplayName(int id, String displayName) {
        var permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id: %s ile eşleşen yetki bulunumadı.".formatted(id)));

        permission.setDisplayName(displayName);
    }
}
