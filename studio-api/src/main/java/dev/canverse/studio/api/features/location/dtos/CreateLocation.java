package dev.canverse.studio.api.features.location.dtos;

import com.google.common.base.CharMatcher;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public class CreateLocation {
    private CreateLocation() {
    }

    @Getter
    @Setter
    public static class Request {
        @Length(min = 3, max = 27, message = "Name must be between {min} and {max} characters.")
        private String name;
        private Integer parentId;

        public void setName(String name) {
            this.name = CharMatcher.whitespace().trimAndCollapseFrom(name, ' ');
        }
    }
}
