package cdy.studioapi.requests;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class RoleCreateRequest {
    @Size(min = 3, max = 50)
    private String name;
    private int level;

    public void setName(String name) {
        this.name = StringUtils.normalizeSpace(name.trim());
    }
}
