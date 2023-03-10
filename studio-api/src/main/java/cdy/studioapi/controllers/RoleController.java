package cdy.studioapi.controllers;

import cdy.studioapi.requests.RoleCreateRequest;
import cdy.studioapi.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public void create(@RequestBody RoleCreateRequest req) {
        roleService.createRole(req);
    }
}
