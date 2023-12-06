package dev.canverse.studio.api.features.reservation.events;

import dev.canverse.studio.api.features.reservation.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreated {
    private Reservation reservation;
}
