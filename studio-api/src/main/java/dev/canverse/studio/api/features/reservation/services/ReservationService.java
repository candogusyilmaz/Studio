package dev.canverse.studio.api.features.reservation.services;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import dev.canverse.studio.api.features.reservation.dtos.CreateReservation;
import dev.canverse.studio.api.features.reservation.dtos.ReservationInfo;
import dev.canverse.studio.api.features.reservation.dtos.UpdateReservation;
import dev.canverse.studio.api.features.reservation.entities.Reservation;
import dev.canverse.studio.api.features.reservation.events.ReservationActionCreated;
import dev.canverse.studio.api.features.reservation.events.ReservationCancelled;
import dev.canverse.studio.api.features.reservation.repositories.ReservationRepository;
import dev.canverse.studio.api.features.reservation.repositories.ReservationSpecifications;
import dev.canverse.studio.api.features.slot.repositories.SlotRepository;
import dev.canverse.studio.api.features.slot.repositories.SlotSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void create(CreateReservation.Request dto) {
        Expect.of(reservationRepository.conflictingWithOthers(dto.getSlotId(), dto.getStartDate(), dto.getEndDate()))
                .isFalse("Slot is already reserved in the specified date range.");
        Expect.of(reservationRepository.conflictingWithSelf(AuthenticationProvider.getAuthentication().getId(), dto.getStartDate(), dto.getEndDate()))
                .isFalse("You already have a reservation in the specified date range.");

        var slot = slotRepository
                .findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found."));

        var reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setUser(AuthenticationProvider.getAuthentication());
        reservation.setDate(dto.getStartDate(), dto.getEndDate());

        reservationRepository.save(reservation);
    }

    @Transactional
    public void update(int reservationId, UpdateReservation.Request dto) {
        var res = reservationRepository
                .findReservationByUserId(AuthenticationProvider.getAuthentication().getId(), reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        Expect.of(reservationRepository.conflictingWithOthers(res.getId(), dto.getSlotId(), dto.getStartDate(), dto.getEndDate()))
                .isFalse("Slot is already reserved in the specified date range.");
        Expect.of(reservationRepository.conflictingWithSelf(res.getId(), res.getUser().getId(), dto.getStartDate(), dto.getEndDate()))
                .isFalse("You already have a reservation in the specified date range.");


        if (!dto.getSlotId().equals(res.getSlot().getId())) {
            var slot = slotRepository
                    .findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first)
                    .orElseThrow(() -> new IllegalArgumentException("Slot not found."));
            res.setSlot(slot);
        }

        res.setDate(dto.getStartDate(), dto.getEndDate());
        reservationRepository.save(res);
    }

    public Page<ReservationInfo> findReservationsByUserId(int userId, Pageable page) {
        return reservationRepository.findByUserId(userId, page).map(ReservationInfo::new);
    }

    @Transactional
    public void cancelReservation(int id) {
        var spec = ReservationSpecifications.findByReservationIdAndUserId(AuthenticationProvider.getAuthentication().getId(), id);

        var reservation = reservationRepository.findBy(spec, r -> r.project("lastAction").first())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        Expect.of(reservation.getEndDate()).after(LocalDateTime.now(), "Since the reservation has ended, it cannot be cancelled.");
        Expect.of(reservation.getLastAction().getStatus().isCancellable()).isTrue("Reservation status is not cancellable.");

        eventPublisher.publishEvent(new ReservationCancelled(reservation));
    }

    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreated event) {
        reservationRepository.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
