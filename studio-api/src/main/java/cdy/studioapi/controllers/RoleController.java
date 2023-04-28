package cdy.studioapi.controllers;

import cdy.studioapi.requests.RoleCreateRequest;
import cdy.studioapi.services.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public void create(@RequestBody @Valid RoleCreateRequest req) {
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
