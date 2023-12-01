package dev.cdy.studio.api.features.authorization;

import dev.cdy.studio.api.features.authorization.dtos.PermissionInfo;
import dev.cdy.studio.api.features.authorization.services.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('list_permissions1')")
    public Page<PermissionInfo> getAll(@PageableDefault Pageable pageable) {
        return permissionService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public PermissionInfo getById(@PathVariable int id) {
        return permissionService.getById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDisplayName(@PathVariable int id, @RequestBody String displayName) {
        permissionService.updateDisplayName(id, displayName);
    }
}
