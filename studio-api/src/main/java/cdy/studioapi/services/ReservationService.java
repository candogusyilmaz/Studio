package cdy.studioapi.services;

import cdy.studioapi.dtos.ReservationCreateDto;
import cdy.studioapi.dtos.ReservationUpdateDto;
import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.events.ReservationUpdateEvent;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.ReservationRepository;
import cdy.studioapi.infrastructure.SlotRepository;
import cdy.studioapi.infrastructure.specs.ReservationSpecifications;
import cdy.studioapi.infrastructure.specs.SlotSpecifications;
import cdy.studioapi.models.Reservation;
import cdy.studioapi.models.User;
import cdy.studioapi.views.ReservationView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void create(ReservationCreateDto dto, int userId) {
        if (conflictingWithOthers(dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (conflictingWithSelf(userId, dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new NotFoundException("Slot bulunamadı."));

        var user = new User(userId);

        var reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setUser(user);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        reservationRepository.save(reservation);
        eventPublisher.publishEvent(new ReservationCreateEvent(reservation));
    }

    public void update(int reservationId, ReservationUpdateDto dto) {
        var res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));

        if (conflictingWithOthers(res.getId(), dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (conflictingWithSelf(res.getId(), res.getUser().getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        if (dto.getSlotId() != res.getSlot().getId()) {
            var slot = slotRepository.findBy(SlotSpecifications.slotIdEquals(dto.getSlotId()), FluentQuery.FetchableFluentQuery::first)
                    .orElseThrow(() -> new NotFoundException("Slot bulunamadı."));
            res.setSlot(slot);
        }

        res.setStartDate(dto.getStartDate());
        res.setEndDate(dto.getEndDate());

        reservationRepository.save(res);
        eventPublisher.publishEvent(new ReservationUpdateEvent(res));
    }

    public List<ReservationView> getAll() {
        return reservationRepository.findAllAsReservationView();
    }

    public Page<List<ReservationView>> getAll(int userId, Pageable page) {
        return reservationRepository.findAllAsReservationView(userId, page);
    }

    public boolean conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate);

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithOthers(int reservationId, int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate)
                .and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate);

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int reservationId, int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate)
                .and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationRepository.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }


    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreateEvent event) {
        reservationRepository.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
