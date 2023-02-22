package cdy.studioapi.services;

import cdy.studioapi.config.Auth;
import cdy.studioapi.enums.ReservationStatus;
import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.events.ReservationCancellationEvent;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.events.ReservationUpdateEvent;
import cdy.studioapi.infrastructure.jpa.ReservationActionJpaRepository;
import cdy.studioapi.models.ReservationAction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ReservationActionService {
    private final ReservationActionJpaRepository actionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReservationActionService(ReservationActionJpaRepository actionRepository, ApplicationEventPublisher eventPublisher) {
        this.actionRepository = actionRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void createActionWhenReservationCreated(ReservationCreateEvent event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(Auth.asUser());
        action.setStatus(ReservationStatus.ACTIVE);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreateEvent(action));
    }

    @EventListener
    public void createActionWhenReservationUpdated(ReservationUpdateEvent event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(Auth.asUser());
        action.setStatus(ReservationStatus.UPDATED);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreateEvent(action));
    }

    @EventListener
    public void createActionWhenReservationCancelled(ReservationCancellationEvent event) {
        var action = new ReservationAction();

        action.setReservation(event.getReservation());
        action.setActionBy(Auth.asUser());
        action.setStatus(ReservationStatus.CANCELLED);
        action.setDescription(event.getReservation().toString());

        actionRepository.save(action);
        eventPublisher.publishEvent(new ReservationActionCreateEvent(action));
    }
}
