package cdy.studio.service.views;

import cdy.studio.core.models.Reservation;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ReservationView implements Serializable {
    private final int id;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private UserBasicView user;
    private SlotView slot;
    private ReservationActionView lastAction;

    public ReservationView(Reservation res) {
        this.id = res.getId();
        this.startDate = res.getStartDate();
        this.endDate = res.getEndDate();

        if (Hibernate.isInitialized(res.getUser())) {
            this.user = new UserBasicView(res.getUser());
        }

        if (Hibernate.isInitialized(res.getSlot())) {
            this.slot = new SlotView(res.getSlot());
        }

        if (Hibernate.isInitialized(res.getLastAction())) {
            this.lastAction = new ReservationActionView(res.getLastAction());
        }
    }
}