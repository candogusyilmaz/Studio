package cdy.studioapi.events;

import cdy.studioapi.models.ReservationAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationActionCreateEvent {
    private ReservationAction action;
}
