package cdy.studio.core.events;

import cdy.studio.core.models.ReservationAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationActionCreateEvent {
    private ReservationAction action;
}