package cdy.studioapi.views;

import cdy.studioapi.models.Reservation;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ReservationView implements Serializable {
    private final int id;

    private final SimpleUserView user;
    private final SlotView slot;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final ReservationActionView lastAction;

    public ReservationView(Reservation res) {
        this.id = res.getId();
        this.user = new SimpleUserView(res.getUser());
        this.slot = new SlotView(res.getSlot());
        this.startDate = res.getStartDate();
        this.endDate = res.getEndDate();
        this.lastAction = new ReservationActionView(res.getLastAction());
    }
}