package dev.canverse.studio.api.startup;

import dev.canverse.studio.api.features.authorization.entities.Role;
import dev.canverse.studio.api.features.authorization.entities.RolePermission;
import dev.canverse.studio.api.features.authorization.repositories.PermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RolePermissionRepository;
import dev.canverse.studio.api.features.authorization.repositories.RoleRepository;
import dev.canverse.studio.api.startup.events.DefaultRolePermissionInitializeComplete;
import dev.canverse.studio.api.startup.events.PermissionInitializerComplete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRolePermissionInitializer implements ApplicationListener<PermissionInitializerComplete> {
    private static final String ADMIN_ROLE = "admin";
    private static final String EVERYONE_ROLE = "everyone";
    private static final String EVERYONE_PREFIX = "everyone_";

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull PermissionInitializerComplete event) {
        var permissions = permissionRepository.findAll();
        var adminRole = getOrCreateRole(ADMIN_ROLE, Integer.MAX_VALUE);
        var everyoneRole = getOrCreateRole(EVERYONE_ROLE, Integer.MIN_VALUE);

        var adminPermissions = permissions.stream()
                .filter(s -> adminRole.getRolePermissions().stream().noneMatch(r -> r.getPermission().equals(s)))
                .map(s -> new RolePermission(adminRole, s))
                .toList();

        var everyonePermissions = permissions.stream()
                .filter(s -> everyoneRole.getRolePermissions().stream().noneMatch(r -> r.getPermission().equals(s)))
                .filter(s -> s.getName().startsWith(EVERYONE_PREFIX))
                .map(s -> new RolePermission(everyoneRole, s))
                .toList();

        var rolePermissions = Stream.concat(adminPermissions.stream(), everyonePermissions.stream()).toList();

        rolePermissionRepository.saveAllAndFlush(rolePermissions);
        log.info("Default roles and permissions initialized.");

        eventPublisher.publishEvent(new DefaultRolePermissionInitializeComplete(this));
    }

    private Role getOrCreateRole(String name, int level) {
        return roleRepository
                .findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name, level)));
    }
}
