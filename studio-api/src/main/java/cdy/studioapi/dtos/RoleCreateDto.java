package cdy.studioapi.dtos;

import cdy.studioapi.models.Role;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class RoleCreateDto {
    private String name;

    public Role asEntity() {
        return new Role(name);
    }

    public String getName() {
        return StringUtils.normalizeSpace(name);
    }
}
