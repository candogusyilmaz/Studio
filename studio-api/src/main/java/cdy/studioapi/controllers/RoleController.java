package cdy.studioapi.controllers;

import cdy.studioapi.dtos.RoleDto;
import cdy.studioapi.requests.RoleCreateRequest;
import cdy.studioapi.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(roleService.getAll(pageable));
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@RequestBody RoleCreateRequest req) {
        var createdRole = roleService.createRole(req);
        var createdRoleDto = RoleDto.from(createdRole);
        return ResponseEntity.ok(createdRoleDto);
    }
}
