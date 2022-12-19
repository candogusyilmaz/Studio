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
        var permission = permissionRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Permission with the id " + req.getId() + " not found"));

        permission.updateDisplayName(req.getDisplayName());

        permissionRepository.save(permission);
    }
}
