package cdy.studioapi.events;

import cdy.studioapi.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCancellationEvent {
    private Reservation reservation;
}
