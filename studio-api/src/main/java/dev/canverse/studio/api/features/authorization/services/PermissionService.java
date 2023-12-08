package dev.canverse.studio.api.features.authorization.services;

import dev.canverse.studio.api.features.authorization.dtos.PermissionInfo;
import dev.canverse.studio.api.features.authorization.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    /**
     * Retrieves a paginated list of all permissions.
     *
     * @param page The pageable information specifying the page size, page number, and sorting.
     * @return A Page containing PermissionInfo objects representing the permissions.
     */
    public Page<PermissionInfo> getAll(Pageable page) {
        return permissionRepository.findBy((root, query, criteriaBuilder) -> null, r -> r.sortBy(page.getSort()).page(page).map(PermissionInfo::new));
    }

    /**
     * Retrieves a PermissionInfo object based on the provided permission ID.
     *
     * @param id The ID of the permission to retrieve.
     * @return A PermissionInfo object representing the permission.
     * @throws IllegalArgumentException Thrown if the permission with the specified ID is not found.
     */
    public PermissionInfo getById(int id) {
        return permissionRepository.findById(id).map(PermissionInfo::new)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found."));
    }

    /**
     * Updates the display name of a permission based on the provided permission ID.
     *
     * @param id          The ID of the permission to be updated.
     * @param displayName The new display name for the permission.
     * @throws IllegalArgumentException Thrown if the permission with the specified ID is not found.
     */
    public void updateDisplayName(int id, String displayName) {
        var permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found."));

        permission.setDisplayName(displayName);
        permissionRepository.saveAndFlush(permission);
    }
}
