package dev.cdy.studio.api.features.reservation.dtos;

import dev.cdy.studio.api.features.reservation.entities.ReservationAction;
import dev.cdy.studio.api.features.reservation.entities.ReservationStatus;
import dev.cdy.studio.api.features.user.dtos.UserSummaryInfo;
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
    private LocalDateTime actionDate;
    private UserSummaryInfo actionBy;

    public ReservationActionInfo(ReservationAction res) {
        if (res == null) return;

        this.id = res.getId();
        this.description = res.getDescription();
        this.status = res.getStatus();
        this.actionDate = res.getActionDate();
        this.actionBy = new UserSummaryInfo(res.getActionBy());
    }
}
