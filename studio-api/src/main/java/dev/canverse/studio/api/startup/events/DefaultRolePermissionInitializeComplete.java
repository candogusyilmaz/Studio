package dev.canverse.studio.api.startup.events;

import org.springframework.context.ApplicationEvent;

public class DefaultRolePermissionInitializeComplete extends ApplicationEvent {
    public DefaultRolePermissionInitializeComplete(Object source) {
        super(source);
    }
}
