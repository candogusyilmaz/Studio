package cdy.studio.service;

import cdy.studio.core.events.ReservationActionCreated;
import cdy.studio.core.events.ReservationCancelled;
import cdy.studio.core.events.ReservationCreated;
import cdy.studio.core.events.ReservationUpdated;
import cdy.studio.core.models.Reservation;
import cdy.studio.core.models.User;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private final AuthenticationProvider authenticationProvider;
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void create(ReservationCreateRequest dto, int userId) {
        if (conflictingWithOthers(dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (conflictingWithSelf(userId, dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first).orElseThrow(() -> new NotFoundException("Slot bulunamadı."));

        var user = new User(userId);

        var reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setUser(user);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        reservationRepository.save(reservation);
        eventPublisher.publishEvent(new ReservationCreated(reservation));
    }

    @Transactional
    public void update(int reservationId, ReservationUpdateRequest dto) {
        var res = reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));

        if (conflictingWithOthers(res.getId(), dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (conflictingWithSelf(res.getId(), res.getUser().getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        if (!dto.getSlotId().equals(res.getSlot().getId())) {
            var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first).orElseThrow(() -> new NotFoundException("Slot bulunamadı."));
            res.setSlot(slot);
        }

        res.setStartDate(dto.getStartDate());
        res.setEndDate(dto.getEndDate());

        reservationRepository.save(res);
        eventPublisher.publishEvent(new ReservationUpdated(res));
    }

    public Set<ReservationView> getAll() {
        return reservationRepository.findAllAsReservationView().stream().map(ReservationView::new).collect(Collectors.toSet());
    }

    public Page<ReservationView> getAll(int userId, Pageable page) {
        return reservationRepository.findAllAsReservationView(userId, page).map(ReservationView::new);
    }

    @Transactional
    public void cancelReservation(int id) {
        var spec = ReservationSpecifications.getUserReservationById(authenticationProvider.getAuthentication().getId(), id);
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

    public boolean conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate);

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithOthers(int reservationId, int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate).and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate);

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int reservationId, int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate).and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreated event) {
        reservationRepository.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
