package cdy.studio.core.events;

import cdy.studio.core.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationUpdated {
    private Reservation reservation;
}
