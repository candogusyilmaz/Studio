package cdy.studio.service;

import cdy.studio.service.exceptions.BadRequestException;
import cdy.studio.service.requests.RoleCreateRequest;
import cdy.studio.core.models.Role;
import cdy.studio.core.models.RolePermission;
import cdy.studio.infrastructure.repositories.PermissionRepository;
import cdy.studio.infrastructure.repositories.RolePermissionRepository;
import cdy.studio.infrastructure.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final AuthenticationProvider authenticationProvider;

    public void create(RoleCreateRequest req) {
        if (req.getLevel() > authenticationProvider.getHighestLevel())
            throw new BadRequestException("Kendinizden yüksek seviye bir rol oluşturamazsınız.");

        if (roleRepository.existsByName(req.getName()))
            throw new BadRequestException("Bu isimde bir rol zaten var.");

        var role = new Role(req.getName(), req.getLevel());
        roleRepository.save(role);
    }

    public void addPermission(int roleId, int permissionId) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new BadRequestException("Rol bulunamadı."));

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