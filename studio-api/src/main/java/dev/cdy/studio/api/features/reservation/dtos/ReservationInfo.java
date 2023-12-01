package dev.cdy.studio.api.features.reservation.dtos;

import dev.cdy.studio.api.features.reservation.entities.Reservation;
import dev.cdy.studio.api.features.slot.dtos.SlotInfo;
import dev.cdy.studio.api.features.user.dtos.UserSummaryInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationInfo implements Serializable {
    private int id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserSummaryInfo user;
    private SlotInfo slot;
    private ReservationActionInfo lastAction;

    public ReservationInfo(Reservation res) {
        if (res == null) return;

        this.id = res.getId();
        this.startDate = res.getStartDate();
        this.endDate = res.getEndDate();
        this.user = new UserSummaryInfo(res.getUser());
        this.slot = new SlotInfo(res.getSlot());
        this.lastAction = new ReservationActionInfo(res.getLastAction());
    }
}