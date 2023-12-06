package dev.canverse.studio.api.features.authorization.services;

import com.google.common.base.Preconditions;
import dev.canverse.studio.api.exceptions.BadRequestException;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import dev.canverse.studio.api.features.authorization.dtos.CreateRole;
import dev.canverse.studio.api.features.authorization.entities.Role;
import dev.canverse.studio.api.features.authorization.entities.RolePermission;
import dev.canverse.studio.api.features.authorization.repositories.PermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RolePermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final AuthenticationProvider authenticationProvider;

    public void create(CreateRole.Request req) {
        Preconditions.checkArgument(authenticationProvider.getHighestLevel() > req.getLevel(), "You cannot create a role higher or equal to your level.");
        Preconditions.checkArgument(!roleRepository.existsByName(req.getName()), "There is already a role with this name.");

        var role = new Role(req.getName(), req.getLevel());
        roleRepository.save(role);
    }

    public void addPermission(int roleId, int permissionId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException("Rol bulunamadı.", roleId));

        if (role.getLevel() > authenticationProvider.getHighestLevel())
            throw new BadRequestException("Kendinizden yüksek seviye bir rolü düzenleyemezsiniz.");

        var permission = permissionRepository.findById(permissionId).orElseThrow(() -> new BadRequestException("İzin bulunamadı."));

        var roleHasPermission = rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId);

        if (roleHasPermission)
            throw new BadRequestException("Bu rolde bu izin zaten var.");

        var rp = new RolePermission(role, permission);
        rolePermissionRepository.save(rp);
    }

    public void removePermission(int roleId, int permissionId) {
        var rolePermission = rolePermissionRepository.findBy((root, query, cb) ->
                                cb.and(
                                        cb.equal(root.get("role").get("id"), roleId),
                                        cb.equal(root.get("permission").get("id"), permissionId))
                        , r -> r.project("role").first())
                .orElseThrow(() -> new BadRequestException("Bu rolde bu izin bulunamadı."));

        if (rolePermission.getRole().getLevel() > authenticationProvider.getHighestLevel())
            throw new BadRequestException("Kendinizden yüksek seviye bir rolü düzenleyemezsiniz.");

        rolePermissionRepository.delete(rolePermission);
    }
}
