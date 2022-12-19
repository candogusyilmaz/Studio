package cdy.studioapi.dtos;

import cdy.studioapi.models.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class RoleDto implements Serializable {
    private final Integer id;
    private final String name;

    public static RoleDto from(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}