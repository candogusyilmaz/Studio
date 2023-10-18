package cdy.studio.service;

import cdy.studio.core.events.ReservationActionCreated;
import cdy.studio.core.events.ReservationCancelled;
import cdy.studio.core.models.Reservation;
import cdy.studio.infrastructure.repositories.ReservationRepository;
import cdy.studio.infrastructure.repositories.SlotRepository;
import cdy.studio.infrastructure.specifications.ReservationSpecifications;
import cdy.studio.infrastructure.specifications.SlotSpecifications;
import cdy.studio.service.exceptions.BadRequestException;
import cdy.studio.service.exceptions.NotFoundException;
import cdy.studio.service.requests.ReservationCreateRequest;
import cdy.studio.service.requests.ReservationUpdateRequest;
import cdy.studio.service.views.ReservationView;
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
    public void create(ReservationCreateRequest dto) {
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
    public void update(int reservationId, ReservationUpdateRequest dto) {
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

    public Page<ReservationView> findReservationsByUserId(int userId, Pageable page) {
        return reservationRepository.findByUserId(userId, page).map(ReservationView::new);
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
