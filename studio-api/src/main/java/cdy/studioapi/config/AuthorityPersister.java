package cdy.studioapi.config;

import cdy.studioapi.StudioApi;
import cdy.studioapi.infrastructure.PermissionRepository;
import cdy.studioapi.models.Permission;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
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
        var controllerMethodPermissions = getPermissions(getControllers());

        // Find the difference between the permissions in the database and the permissions in the controllers
        var newPermissions = controllerMethodPermissions.stream()
                .filter(p -> persistedPermissions.stream().noneMatch(pp -> p.getName().equals(pp.getName())))
                .toList();

        // Persist the new permissions
        permissionRepository.saveAllAndFlush(newPermissions);
    }

    private static List<Class<?>> getControllers() {
        ClassLoader classLoader = StudioApi.class.getClassLoader();
        String packageName = "cdy.studioapi.controllers";
        String packagePath = packageName.replace('.', '/');

        List<Class<?>> classes = new ArrayList<>();
        File packageDir = new File(Objects.requireNonNull(classLoader.getResource(packagePath)).getFile());

        for (File file : Objects.requireNonNull(packageDir.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = null;
                try {
                    clazz = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    logger.log(java.util.logging.Level.SEVERE, e.getMessage(), e);
                }
                classes.add(clazz);
            }
        }

        return classes;
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

    private static List<Permission> getPermissions(List<Class<?>> classes) {
        var permissions = classes.stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(m -> m.isAnnotationPresent(PreAuthorize.class))
                .map(m -> {
                    var annotation = m.getAnnotation(PreAuthorize.class);
                    return getMethodAuthorities(annotation.value());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return permissions.stream().map(p -> Permission.create(p, p)).toList();
    }
}
