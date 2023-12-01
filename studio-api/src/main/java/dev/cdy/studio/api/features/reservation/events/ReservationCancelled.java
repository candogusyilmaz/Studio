package dev.cdy.studio.api.features.reservation.events;

import dev.cdy.studio.api.features.reservation.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCancelled {
    private Reservation reservation;
}
