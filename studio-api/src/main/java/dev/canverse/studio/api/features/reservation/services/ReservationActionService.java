package dev.canverse.studio.api.features.reservation.services;

import dev.canverse.studio.api.features.reservation.entities.ReservationAction;
import dev.canverse.studio.api.features.reservation.entities.ReservationStatus;
import dev.canverse.studio.api.features.reservation.events.ReservationActionCreated;
import dev.canverse.studio.api.features.reservation.events.ReservationCancelled;
import dev.canverse.studio.api.features.reservation.events.ReservationCreated;
import dev.canverse.studio.api.features.reservation.events.ReservationUpdated;
import dev.canverse.studio.api.features.reservation.repositories.ReservationActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationActionService {
    private final ReservationActionRepository actionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void createActionWhenReservationCreated(ReservationCreated event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setStatus(ReservationStatus.ACTIVE);

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }

    @EventListener
    public void createActionWhenReservationUpdated(ReservationUpdated event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setStatus(ReservationStatus.UPDATED);

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }

    @EventListener
    public void createActionWhenReservationCancelled(ReservationCancelled event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setStatus(ReservationStatus.CANCELLED);

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreated(action));
    }
}
