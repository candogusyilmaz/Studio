package cdy.studioapi.services;

import cdy.studioapi.dtos.ReservationCreateDto;
import cdy.studioapi.dtos.ReservationUpdateDto;
import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.events.ReservationUpdateEvent;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.jpa.ReservationJpaRepository;
import cdy.studioapi.infrastructure.jpa.SlotJpaRepository;
import cdy.studioapi.models.Reservation;
import cdy.studioapi.models.User;
import cdy.studioapi.views.ReservationView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationJpaRepository reservationRepository;
    private final SlotJpaRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void create(ReservationCreateDto dto, int userId) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Başlangıç tarihi bitiş tarihinden sonra olmalıdır.");
        }

        if (Duration.between(dto.getStartDate(), dto.getEndDate()).toMinutes() < Duration.ofMinutes(10).toMinutes()) {
            throw new BadRequestException("Başlangıç tarihi ile bitiş tarihi arasında en az 10 dakika olmalıdır.");
        }

        var conflicting = reservationRepository.hasActiveReservationBetweenDates(userId, dto.getStartDate(), dto.getEndDate());

        if (conflicting) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        var slot = slotRepository.findById(dto.getSlotId())
                .orElseThrow(() -> new NotFoundException("Slot bulunamadı."));

        var reservable = reservationRepository.slotReservableBetweenDates(dto.getSlotId(), dto.getStartDate(), dto.getEndDate());

        if (!reservable) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

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
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Başlangıç tarihi bitiş tarihinden sonra olmalıdır.");
        }

        if (Duration.between(dto.getStartDate(), dto.getEndDate()).toMinutes() < Duration.ofMinutes(10).toMinutes()) {
            throw new BadRequestException("Başlangıç tarihi ile bitiş tarihi arasında en az 10 dakika olmalıdır.");
        }

        var res = reservationRepository.findBy(
                        (root, query, cb) -> cb.equal(root.get("id"), reservationId),
                        r -> r.project("slot", "user").first())
                .orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));

        var conflictingWithOthers = reservationRepository.findBy(
                (root, query, cb) -> cb.and(
                        cb.notEqual(root.get("id"), res.getId()),
                        cb.equal(root.get("slot").get("id"), dto.getSlotId()),
                        cb.lessThanOrEqualTo(root.get("startDate"), dto.getEndDate()),
                        cb.greaterThanOrEqualTo(root.get("endDate"), dto.getStartDate())
                ),
                FluentQuery.FetchableFluentQuery::exists);

        if (conflictingWithOthers) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        var conflictingWithSelf = reservationRepository.findBy(
                (root, query, cb) -> cb.and(
                        cb.notEqual(root.get("id"), res.getId()),
                        cb.equal(root.get("user").get("id"), res.getUser().getId()),
                        cb.lessThanOrEqualTo(root.get("startDate"), dto.getEndDate()),
                        cb.greaterThanOrEqualTo(root.get("endDate"), dto.getStartDate())
                ),
                FluentQuery.FetchableFluentQuery::exists);

        if (conflictingWithSelf) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        if (dto.getSlotId() != res.getSlot().getId()) {
            var slot = slotRepository.findBy(
                            (root, query, cb) -> cb.equal(root.get("id"), dto.getSlotId()),
                            FluentQuery.FetchableFluentQuery::first)
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

    public List<ReservationView> getAll(int userId) {
        return reservationRepository.findAllAsReservationView(userId);
    }

    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreateEvent event) {
        reservationRepository.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
