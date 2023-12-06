package dev.canverse.studio.api.features.authorization.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class CreateRole {
    private CreateRole() {
    }

    @Setter
    @Getter
    public static class Request {
        @Size(min = 3, max = 50)
        private String name;
        private Integer level;

        public void setName(String name) {
            this.name = StringUtils.normalizeSpace(name.trim());
        }
    }
}
