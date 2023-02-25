package cdy.studioapi.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Getter
@Setter
public class LocationCreateDto {
    @Length(min = 3, max = 27, message = "Lokasyon ismi 3 ila 27 karakter arasında olmalıdır.")
    private String name;
    @Positive(message = "Üst lokasyon bulunamadı.")
    private Optional<Integer> parentId;

    public String getName() {
        return StringUtils.normalizeSpace(name);
    }
}
