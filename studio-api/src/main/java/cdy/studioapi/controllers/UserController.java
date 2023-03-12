package cdy.studioapi.controllers;

import cdy.studioapi.services.UserService;
import cdy.studioapi.views.UserView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserView> getAll(@PageableDefault Pageable pageable) {
        return userService.getAll(pageable);
    }
}
