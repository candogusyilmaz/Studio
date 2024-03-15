package dev.canverse.studio.api.startup;

import dev.canverse.studio.api.features.authorization.repositories.RoleRepository;
import dev.canverse.studio.api.features.shared.TimePeriod;
import dev.canverse.studio.api.features.user.entities.User;
import dev.canverse.studio.api.features.user.entities.UserRole;
import dev.canverse.studio.api.features.user.repositories.UserRepository;
import dev.canverse.studio.api.features.user.repositories.UserRoleRepository;
import dev.canverse.studio.api.startup.events.DefaultRolePermissionInitializeComplete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements ApplicationListener<DefaultRolePermissionInitializeComplete> {
    private static final String ADMIN_PASSWORD = "123";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull DefaultRolePermissionInitializeComplete event) {
        var adminUser = userRepository.findByUsername("admin")
                .orElseGet(() -> {
                    User admin = new User("admin", passwordEncoder.encode(ADMIN_PASSWORD));
                    admin.setDisplayName("Admin");
                    return userRepository.save(admin);
                });

        var adminRole = roleRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("Admin role not found."));

        if (userRoleRepository.existsBy(adminUser.getId(), adminRole.getId()))
            return;

        UserRole adminUserRole = new UserRole(adminUser, adminRole);
        adminUserRole.setTimePeriod(new TimePeriod(LocalDateTime.now(), LocalDateTime.now().plusYears(2934)));
        userRoleRepository.save(adminUserRole);
        log.info("Admin user and role initialized.");
    }
}
