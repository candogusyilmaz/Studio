package dev.canverse.studio.api.features.authorization.services;

import com.google.common.base.Preconditions;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
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
    private final AuthenticationProvider authenticationProvider;

    /**
     * Creates a new role based on the provided request.
     *
     * @param req The request containing the details of the role to be created.
     * @throws IllegalArgumentException Thrown if the user's level is not sufficient or if a role with the same name already exists.
     */
    public void create(CreateRole.Request req) {
        Preconditions.checkArgument(authenticationProvider.getHighestLevel() >= req.getLevel(), "You cannot create a role higher or equal to your level.");
        Preconditions.checkArgument(!roleRepository.existsByName(req.getName()), "There is already a role with this name.");

        var role = new Role(req.getName(), req.getLevel());
        roleRepository.save(role);
    }

    /**
     * Adds a permission to a role based on the provided role ID and permission ID.
     *
     * @param roleId       The ID of the role to which the permission is to be added.
     * @param permissionId The ID of the permission to be added.
     * @throws IllegalArgumentException Thrown if the role or permission is not found.
     * @throws IllegalStateException    Thrown if the user's level is not sufficient or if the role already has the permission.
     */
    public void addPermission(int roleId, int permissionId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found."));

        Preconditions.checkState(authenticationProvider.getHighestLevel() >= role.getLevel(),
                "You cannot add a permission to a role higher or equal to your level.");
        Preconditions.checkState(!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId),
                "Role already has this permission.");

        var permission = permissionRepository.findById(permissionId).orElseThrow(() -> new IllegalArgumentException("Permission not found."));
        var rp = new RolePermission(role, permission);

        rolePermissionRepository.save(rp);
    }

    /**
     * Removes a permission from a role based on the provided role ID and permission ID.
     *
     * @param roleId       The ID of the role from which the permission is to be removed.
     * @param permissionId The ID of the permission to be removed.
     * @throws IllegalArgumentException Thrown if the role does not have the specified permission.
     * @throws IllegalStateException    Thrown if the user's level is not sufficient to perform the operation.
     */
    public void removePermission(int roleId, int permissionId) {
        var rolePermission = rolePermissionRepository.findBy((root, query, cb) ->
                                cb.and(
                                        cb.equal(root.get("role").get("id"), roleId),
                                        cb.equal(root.get("permission").get("id"), permissionId))
                        , r -> r.project("role").first())
                .orElseThrow(() -> new IllegalArgumentException("Role has no such permission."));

        Preconditions.checkState(authenticationProvider.getHighestLevel() >= rolePermission.getRole().getLevel(),
                "You cannot remove a permission from a role higher or equal to your level.");

        rolePermissionRepository.delete(rolePermission);
    }
}
