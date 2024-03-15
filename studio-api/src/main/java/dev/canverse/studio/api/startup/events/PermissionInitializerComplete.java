package dev.canverse.studio.api.startup.events;

import org.springframework.context.ApplicationEvent;

public class PermissionInitializerComplete extends ApplicationEvent {
    public PermissionInitializerComplete(Object source) {
        super(source);
    }
}
