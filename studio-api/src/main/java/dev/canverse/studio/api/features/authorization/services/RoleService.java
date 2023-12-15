package dev.canverse.studio.api.features.authorization.services;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.authorization.dtos.CreateRole;
import dev.canverse.studio.api.features.authorization.entities.Role;
import dev.canverse.studio.api.features.authorization.entities.RolePermission;
import dev.canverse.studio.api.features.authorization.repositories.PermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RolePermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Creates a new role based on the provided request.
     *
     * @param req The request containing the details of the role to be created.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if a role with the same name already exists.
     */
    public void create(CreateRole.Request req) {
        Expect.of(roleRepository.existsByName(req.getName()))
                .isFalse("There is already a role with this name.");

        var role = new Role(req.getName(), req.getLevel());
        roleRepository.save(role);
    }

    /**
     * Adds a permission to a role based on the provided role ID and permission ID.
     *
     * @param roleId       The ID of the role to which the permission is to be added.
     * @param permissionId The ID of the permission to be added.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if the role or permission is not found.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if the role already has the permission.
     */
    public void addPermission(int roleId, int permissionId) {
        var role = Expect.of(roleRepository.findById(roleId)).present("Role not found.");

        Expect.of(rolePermissionRepository.exists(roleId, permissionId)).isFalse("Role already has this permission.");

        var permission = Expect.of(permissionRepository.findById(permissionId)).present("Permission not found.");
        var rp = new RolePermission(role, permission);

        rolePermissionRepository.save(rp);
    }

    /**
     * Removes a permission from a role based on the provided role ID and permission ID.
     *
     * @param roleId       The ID of the role from which the permission is to be removed.
     * @param permissionId The ID of the permission to be removed.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if the role/permission combination is not found.
     */
    public void removePermission(int roleId, int permissionId) {
        var rolePermission = Expect.of(rolePermissionRepository.findById(roleId, permissionId)).present("Role/permission combination not found.");

        rolePermissionRepository.delete(rolePermission);
    }
}
