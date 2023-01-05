package cdy.studioapi.dtos;

import cdy.studioapi.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateDto {
    private String name;

    public Role asEntity() {
        return new Role(name);
    }
}
