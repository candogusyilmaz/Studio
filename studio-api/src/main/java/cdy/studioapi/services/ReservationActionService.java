package cdy.studioapi.services;

import cdy.studioapi.enums.ReservationStatus;
import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.infrastructure.ReservationActionRepository;
import cdy.studioapi.models.ReservationAction;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationActionService {
    private final ReservationActionRepository actionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void createActionWhenReservationCreated(ReservationCreateEvent event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(event.getReservation().getUser());
        action.setStatus(ReservationStatus.ACTIVE);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreateEvent(action));
    }
}
