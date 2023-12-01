package dev.cdy.studio.api.features.location.dtos;

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
        @Length(min = 3, max = 27, message = "Lokasyon ismi 3 ila 27 karakter arasında olmalıdır.")
        private String name;
        private Integer parentId;

        public void setName(String name) {
            this.name = CharMatcher.whitespace().trimAndCollapseFrom(name, ' ');
        }
    }
}
