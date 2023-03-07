package cdy.studioapi.requests;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
public class RoleCreateRequest {
    private String name;

    public String getName() {
        return StringUtils.normalizeSpace(name);
    }
}
