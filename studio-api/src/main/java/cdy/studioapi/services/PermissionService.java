package cdy.studioapi.services;

import cdy.studioapi.infrastructure.PermissionRepository;
import cdy.studioapi.requests.PermissionUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public void updateDisplayName(PermissionUpdateRequest req) {
        var permission = permissionRepository.findById(req.id())
                .orElseThrow(() -> new RuntimeException("Permission with the id " + req.id() + " not found"));

        permission.updateDisplayName(req.displayName());

        permissionRepository.save(permission);
    }
}
