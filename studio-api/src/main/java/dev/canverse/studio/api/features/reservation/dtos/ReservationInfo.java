package dev.canverse.studio.api.features.reservation.dtos;

import dev.canverse.studio.api.features.reservation.entities.Reservation;
import dev.canverse.studio.api.features.shared.TimePeriod;
import dev.canverse.studio.api.features.slot.dtos.SlotInfo;
import dev.canverse.studio.api.features.user.dtos.UserSummaryInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ReservationInfo implements Serializable {
    private int id;
    private TimePeriod timePeriod;
    private UserSummaryInfo user;
    private SlotInfo slot;
    private ReservationActionInfo lastAction;

    public ReservationInfo(Reservation res) {
        if (res == null) return;

        this.id = res.getId();
        this.timePeriod = res.getTimePeriod();
        this.user = new UserSummaryInfo(res.getUser());
        this.slot = new SlotInfo(res.getSlot());
        this.lastAction = new ReservationActionInfo(res.getLastAction());
    }
}