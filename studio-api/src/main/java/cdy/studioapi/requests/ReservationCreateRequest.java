package cdy.studioapi.requests;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateRequest {
    @Positive(message = "Slot bulunamadı.")
    @NotNull(message = "Slot boş bırakılamaz.")
    private int slotId;

    @FutureOrPresent(message = "Başlangıç tarihi gelecekte olmalıdır.")
    @NotNull(message = "Başlangıç tarihi boş bırakılamaz.")
    private LocalDateTime startDate;

    @FutureOrPresent(message = "Bitiş tarihi gelecekte olmalıdır.")
    @NotNull(message = "Bitiş tarihi boş bırakılamaz.")
    private LocalDateTime endDate;
}
