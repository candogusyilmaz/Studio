package dev.canverse.studio.api.features.reservation.events;

import dev.canverse.studio.api.features.reservation.entities.ReservationAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationActionCreated {
    private ReservationAction action;
}
