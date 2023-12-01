package dev.cdy.studio.api.features.reservation.services;

import dev.cdy.studio.api.exceptions.BadRequestException;
import dev.cdy.studio.api.exceptions.NotFoundException;
import dev.cdy.studio.api.features.authentication.AuthenticationProvider;
import dev.cdy.studio.api.features.reservation.dtos.CreateReservation;
import dev.cdy.studio.api.features.reservation.dtos.ReservationInfo;
import dev.cdy.studio.api.features.reservation.dtos.UpdateReservation;
import dev.cdy.studio.api.features.reservation.entities.Reservation;
import dev.cdy.studio.api.features.reservation.events.ReservationActionCreated;
import dev.cdy.studio.api.features.reservation.events.ReservationCancelled;
import dev.cdy.studio.api.features.reservation.repositories.ReservationRepository;
import dev.cdy.studio.api.features.reservation.repositories.ReservationSpecifications;
import dev.cdy.studio.api.features.slot.repositories.SlotRepository;
import dev.cdy.studio.api.features.slot.repositories.SlotSpecifications;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReservationService {
    private final AuthenticationProvider authenticationProvider;
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void create(CreateReservation.Request dto) {
        if (reservationRepository.conflictingWithOthers(dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (reservationRepository.conflictingWithSelf(authenticationProvider.getAuthentication().getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first).orElseThrow(() -> new NotFoundException("Slot bulunamadı."));

        var reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setUser(authenticationProvider.getAuthentication());
        reservation.setDate(dto.getStartDate(), dto.getEndDate());

        reservationRepository.save(reservation);
    }

    @Transactional
    public void update(int reservationId, UpdateReservation.Request dto) {
        var res = reservationRepository.findReservationByUserId(authenticationProvider.getAuthentication().getId(), reservationId)
                .orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));

        if (reservationRepository.conflictingWithOthers(res.getId(), dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (reservationRepository.conflictingWithSelf(res.getId(), res.getUser().getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        if (!dto.getSlotId().equals(res.getSlot().getId())) {
            var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first).orElseThrow(() -> new NotFoundException("Slot bulunamadı."));
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
        var spec = ReservationSpecifications.findByReservationIdAndUserId(authenticationProvider.getAuthentication().getId(), id);

        var reservation = reservationRepository.findBy(spec, r -> r.project("lastAction").first())
                .orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));

        if (reservation.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Rezervasyon tarihi geçtiğinden dolayı iptal edilemez!");
        }

        if (!reservation.getLastAction().getStatus().isCancellable()) {
            throw new BadRequestException("Rezervasyon iptal edilebilir bir durumda değil!");
        }

        eventPublisher.publishEvent(new ReservationCancelled(reservation));
    }

    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreated event) {
        reservationRepository.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
