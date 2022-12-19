package cdy.studioapi.requests;

import cdy.studioapi.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateRequest {
    private String name;

    public Role asEntity() {
        return new Role(name);
    }
}
