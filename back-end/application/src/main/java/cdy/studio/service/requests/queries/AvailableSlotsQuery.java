package cdy.studio.service.requests.queries;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AvailableSlotsQuery {
    private Integer roomId;

    @NotNull(message = "Başlangıç tarihi dolu olmalıdır.")
    private LocalDateTime startDate;

    @NotNull(message = "Bitiş tarihi dolu olmalıdır.")
    private LocalDateTime endDate;
}
