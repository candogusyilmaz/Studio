package dev.canverse.studio.api.features.authorization;

import dev.canverse.studio.api.features.authorization.dtos.CreateRole;
import dev.canverse.studio.api.features.authorization.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public void create(@RequestBody @Valid CreateRole.Request req) {
        roleService.create(req);
    }

    @PostMapping("/{roleId}/add-permission")
    public void addPermission(@PathVariable int roleId, @RequestBody int permissionId) {
        roleService.addPermission(roleId, permissionId);
    }

    @DeleteMapping("/{roleId}/remove-permission")
    public void removePermission(@PathVariable int roleId, @RequestBody int permissionId) {
        roleService.removePermission(roleId, permissionId);
    }
}
