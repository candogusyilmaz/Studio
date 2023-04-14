package cdy.studioapi.config;

import cdy.studioapi.infrastructure.PermissionRepository;
import cdy.studioapi.models.Permission;
import com.google.common.reflect.ClassPath;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthorityPersister implements ApplicationListener<ApplicationReadyEvent> {
    private final PermissionRepository permissionRepository;
    private static final Logger logger = Logger.getLogger(AuthorityPersister.class.getName());

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        var persistedPermissions = permissionRepository.findAll();
        var controllers = getControllers();
        var controllerMethodPermissions = getPermissions(controllers);

        // Find the difference between the permissions in the database and the permissions in the controllers
        var newPermissions = controllerMethodPermissions.stream()
                .filter(p -> persistedPermissions.stream().noneMatch(pp -> p.getName().equals(pp.getName())))
                .toList();

        if (!newPermissions.isEmpty())
            logger.log(Level.INFO, "New permissions found: {0}", newPermissions.stream().map(Permission::getName).collect(Collectors.joining(", ")));

        // Find the permissions that are not used in the controllers
        var unusedPermissions = persistedPermissions.stream()
                .filter(p -> controllerMethodPermissions.stream().noneMatch(pp -> p.getName().equals(pp.getName())))
                .toList();
        if (!unusedPermissions.isEmpty())
            logger.log(Level.INFO, "Unused permissions found: {0}", unusedPermissions.stream().map(Permission::getName).collect(Collectors.joining(", ")));


        if (!newPermissions.isEmpty()) {
            // Persist the new permissions
            permissionRepository.saveAllAndFlush(newPermissions);
            logger.log(Level.INFO, "{0} new permissions persisted.", newPermissions.size());
        }

        if (!unusedPermissions.isEmpty()) {
            // Delete the unused permissions
            permissionRepository.deleteAll(unusedPermissions);
            logger.log(Level.INFO, "{0} unused permissions deleted.", unusedPermissions.size());
        }
    }

    // TODO: Dosya yolunda eğer boşluk varsa hata fırlatıyor başka karakterlerde de aynı sorun çıkabilir
    // TODO: Daha iyi bir yöntem bul!
    private static Set<Class<?>> getControllers() {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream()
                    .filter(clazz -> clazz.getPackageName()
                            .equalsIgnoreCase("cdy.studioapi.controllers"))
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
                        throw new IllegalArgumentException("Yetki adı alfanümerik olmalıdır.");
                    return new Permission(p, p);
                })
                .toList();
    }
}
