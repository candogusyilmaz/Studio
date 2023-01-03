package cdy.studioapi.dtos.queries;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SlotCriteria {
    private Integer roomId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
