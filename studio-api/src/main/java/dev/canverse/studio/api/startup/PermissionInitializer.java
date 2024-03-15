package dev.canverse.studio.api.startup;

import com.google.common.reflect.ClassPath;
import dev.canverse.studio.api.features.authorization.entities.Permission;
import dev.canverse.studio.api.features.authorization.repositories.PermissionRepository;
import dev.canverse.studio.api.startup.events.PermissionInitializerComplete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final PermissionRepository permissionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        var controllers = getControllers();
        var permissionsOnControllers = getPermissions(controllers);
        var persistedPermissions = permissionRepository.findAll();

        savePermissions(persistedPermissions, permissionsOnControllers);
        deletePermissions(persistedPermissions, permissionsOnControllers);
        eventPublisher.publishEvent(new PermissionInitializerComplete(this));
    }

    private void savePermissions(List<Permission> persistedPermissions, List<Permission> permissionsOnControllers) {
        // Permissions that are not persisted in the database but used in the controllers
        var newPermissions = permissionsOnControllers.stream()
                .filter(p -> persistedPermissions.stream().noneMatch(pp -> p.getName().equals(pp.getName())))
                .toList();

        if (newPermissions.isEmpty())
            return;

        permissionRepository.saveAll(newPermissions);
        log.info("{} new permissions persisted.", newPermissions.size());
    }

    private void deletePermissions(List<Permission> persistedPermissions, List<Permission> permissionsOnControllers) {
        // Permissions that are not used in the controllers but persisted in the database
        var unusedPermissions = persistedPermissions.stream()
                .filter(p -> permissionsOnControllers.stream().noneMatch(pp -> p.getName().equals(pp.getName())))
                .toList();

        if (unusedPermissions.isEmpty())
            return;

        permissionRepository.deleteAll(unusedPermissions);
        log.info("{} permissions deleted.", unusedPermissions.size());
    }

    private static Set<Class<?>> getControllers() {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(clazz -> clazz.getPackageName().startsWith("dev.canverse.studio.api") && clazz.getSimpleName().endsWith("Controller"))
                    .map(ClassPath.ClassInfo::load)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while loading Controllers", e);
        }
    }

    private static List<String> getMethodAuthorities(String query) {
        var pattern = Pattern.compile("'([^']+)'");
        var matcher = pattern.matcher(query);
        var list = new ArrayList<String>();

        while (matcher.find()) {
            list.add(matcher.group().replace("'", ""));
        }

        return list;
    }

    private static List<Permission> getPermissions(Set<Class<?>> classes) {
        var permissions = classes.stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(PreAuthorize.class))
                .map(m -> {
                    var annotation = m.getAnnotation(PreAuthorize.class);
                    return getMethodAuthorities(annotation.value());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return permissions.stream()
                .map(p -> {
                    if (!StringUtils.isAlphanumeric(p.replace("_", "")))
                        throw new IllegalArgumentException("Permission name must be alphanumeric.");
                    return new Permission(p, p);
                })
                .toList();
    }
}
