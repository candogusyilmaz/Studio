package cdy.studio.service;

import cdy.studio.core.enums.ReservationStatus;
import cdy.studio.core.events.ReservationActionCreated;
import cdy.studio.core.events.ReservationCancelled;
import cdy.studio.core.events.ReservationCreated;
import cdy.studio.core.events.ReservationUpdated;
import cdy.studio.core.models.ReservationAction;
import cdy.studio.infrastructure.repositories.ReservationActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationActionService {
    private final AuthenticationProvider authenticationProvider;
    private final ReservationActionRepository actionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void createActionWhenReservationCreated(ReservationCreated event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(authenticationProvider.getAuthentication());
        action.setStatus(ReservationStatus.ACTIVE);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }

    @EventListener
    public void createActionWhenReservationUpdated(ReservationUpdated event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(authenticationProvider.getAuthentication());
        action.setStatus(ReservationStatus.UPDATED);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }

    @EventListener
    public void createActionWhenReservationCancelled(ReservationCancelled event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(authenticationProvider.getAuthentication());
        action.setStatus(ReservationStatus.CANCELLED);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }
}
