package dev.cdy.studio.api.features.reservation.services;

import dev.cdy.studio.api.features.authentication.AuthenticationProvider;
import dev.cdy.studio.api.features.reservation.entities.ReservationAction;
import dev.cdy.studio.api.features.reservation.entities.ReservationStatus;
import dev.cdy.studio.api.features.reservation.events.ReservationActionCreated;
import dev.cdy.studio.api.features.reservation.events.ReservationCancelled;
import dev.cdy.studio.api.features.reservation.events.ReservationCreated;
import dev.cdy.studio.api.features.reservation.events.ReservationUpdated;
import dev.cdy.studio.api.features.reservation.repositories.ReservationActionRepository;
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
