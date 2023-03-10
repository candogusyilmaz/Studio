package cdy.studioapi.requests;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class LocationCreateRequest {
    @Length(min = 3, max = 27, message = "Lokasyon ismi 3 ila 27 karakter arasında olmalıdır.")
    private String name;

    private Integer parentId;

    public String getName() {
        return StringUtils.normalizeSpace(name);
    }
}
