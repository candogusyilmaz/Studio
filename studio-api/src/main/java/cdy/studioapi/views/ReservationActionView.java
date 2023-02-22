package cdy.studioapi.views;

import cdy.studioapi.enums.ReservationStatus;
import cdy.studioapi.models.ReservationAction;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ReservationActionView implements Serializable {
    private final int id;
    private final String description;
    private final ReservationStatus status;
    private final LocalDateTime actionDate;
    private UserBasicView actionBy;

    public ReservationActionView(ReservationAction res) {
        this.id = res.getId();
        this.description = res.getDescription();
        this.status = res.getStatus();
        this.actionDate = res.getActionDate();

        if (Hibernate.isInitialized(res.getActionBy())) {
            this.actionBy = new UserBasicView(res.getActionBy());
        }
    }
}
