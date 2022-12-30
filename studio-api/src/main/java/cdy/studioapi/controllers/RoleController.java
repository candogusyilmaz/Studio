package cdy.studioapi.controllers;

import cdy.studioapi.requests.RoleCreateRequest;
import cdy.studioapi.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody RoleCreateRequest req) {
        roleService.createRole(req);
    }
}
