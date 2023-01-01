package cdy.studioapi.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationCreateDto {
    @Positive(message = "Slot bulunamadı.")
    private int slotId;

    @FutureOrPresent(message = "Başlangıç tarihi gelecekte olmalıdır.")
    private LocalDateTime startDate;

    @FutureOrPresent(message = "Bitiş tarihi gelecekte olmalıdır.")
    private LocalDateTime endDate;
}
