package cdy.studioapi.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class RoomCreateDto {
    @Length(min = 3, max = 27, message = "Oda ismi 3 ila 27 karakter arasında olmalıdır.")
    private String name;

    @Positive(message = "Oda kapasitesi 0'dan büyük olmalıdır.")
    private int capacity;

    @Positive(message = "Lokasyon bulunamadı.")
    private int locationId;
}
