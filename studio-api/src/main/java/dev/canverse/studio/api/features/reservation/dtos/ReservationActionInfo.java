package dev.canverse.studio.api.features.reservation.dtos;

import dev.canverse.studio.api.features.reservation.entities.ReservationAction;
import dev.canverse.studio.api.features.reservation.entities.ReservationStatus;
import dev.canverse.studio.api.features.user.dtos.UserSummaryInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationActionInfo implements Serializable {
    private int id;
    private String description;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private UserSummaryInfo createdBy;

    public ReservationActionInfo(ReservationAction res) {
        if (res == null) return;

        this.id = res.getId();
        this.description = res.getDescription();
        this.status = res.getStatus();
        this.createdAt = res.getCreatedAt();
        this.createdBy = new UserSummaryInfo(res.getCreatedBy());
    }
}
