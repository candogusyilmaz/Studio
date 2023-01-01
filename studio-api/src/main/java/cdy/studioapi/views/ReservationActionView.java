package cdy.studioapi.views;

import cdy.studioapi.enums.ReservationStatus;
import cdy.studioapi.models.ReservationAction;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ReservationActionView implements Serializable {
    private final int id;
    private final String description;
    private final ReservationStatus status;
    private final SimpleUserView actionBy;
    private final LocalDateTime actionDate;

    public ReservationActionView(ReservationAction res) {
        this.id = res.getId();
        this.description = res.getDescription();
        this.status = res.getStatus();
        this.actionBy = new SimpleUserView(res.getActionBy());
        this.actionDate = res.getActionDate();
    }
}
