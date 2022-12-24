package cdy.studioapi.dtos;

import cdy.studioapi.models.Role;

import java.io.Serializable;

public record RoleDto(int id, String name) implements Serializable {
    public static RoleDto from(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}