package dev.canverse.studio.api.features.room.dtos;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

public class CreateRoom {
    private CreateRoom() {
    }

    @Getter
    @Setter
    public static class Request {
        @Length(min = 3, max = 27, message = "Oda ismi 3 ila 27 karakter arasında olmalıdır.")
        private String name;

        @Positive(message = "Oda kapasitesi 0'dan büyük olmalıdır.")
        private Integer capacity;

        @Positive(message = "Lokasyon bulunamadı.")
        private Integer locationId;

        public void setName(String name) {
            this.name = StringUtils.normalizeSpace(name.trim());
        }
    }
}