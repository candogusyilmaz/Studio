package dev.canverse.studio.api.features.slot.dtos;

import dev.canverse.studio.api.features.slot.entities.Slot;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AvailableSlots {
    private AvailableSlots() {
    }

    @Getter
    @Setter
    public static class Request {
        private Integer roomId;

        @NotNull(message = "Başlangıç tarihi dolu olmalıdır.")
        private LocalDateTime startDate;

        @NotNull(message = "Bitiş tarihi dolu olmalıdır.")
        private LocalDateTime endDate;
    }

    public static class Response extends SlotInfo {
        public Response(Slot slot) {
            super(slot);
        }
    }
}
