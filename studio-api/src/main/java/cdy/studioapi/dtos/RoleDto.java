package cdy.studioapi.dtos;

import cdy.studioapi.models.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public record RoleDto(Integer id, String name) implements Serializable {
    public static RoleDto from(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}