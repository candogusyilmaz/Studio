package cdy.studioapi.services;

import cdy.studioapi.dtos.ReservationCreateDto;
import cdy.studioapi.dtos.ReservationUpdateDto;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.events.ReservationUpdateEvent;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.infrastructure.ReservationRepository;
import cdy.studioapi.infrastructure.SlotRepository;
import cdy.studioapi.models.Reservation;
import cdy.studioapi.models.User;
import cdy.studioapi.views.ReservationView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void create(ReservationCreateDto dto, int userId) {
        reservationUpsertPreChecks(dto.getStartDate(), dto.getEndDate());

        if (reservationRepository.conflictingWithOthers(dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (reservationRepository.conflictingWithSelf(userId, dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        var slot = slotRepository.getById(dto.getSlotId());

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
        reservationUpsertPreChecks(dto.getStartDate(), dto.getEndDate());

        var res = reservationRepository.getByIdIncludeUserSlot(reservationId);

        if (reservationRepository.conflictingWithOthers(res.getId(), dto.getSlotId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Seçilen slot belirtilen tarihler arasında rezerve edilmiştir.");
        }

        if (reservationRepository.conflictingWithSelf(res.getId(), res.getUser().getId(), dto.getStartDate(), dto.getEndDate())) {
            throw new BadRequestException("Belirtilen tarihler arasında bir rezervasyonunuz bulunmaktadır.");
        }

        if (dto.getSlotId() != res.getSlot().getId()) {
            var slot = slotRepository.getById(dto.getSlotId());
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


    private static void reservationUpsertPreChecks(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Başlangıç tarihi bitiş tarihinden sonra olmalıdır.");
        }

        if (Duration.between(startDate, endDate).toMinutes() < Duration.ofMinutes(10).toMinutes()) {
            throw new BadRequestException("Başlangıç tarihi ile bitiş tarihi arasında en az 10 dakika olmalıdır.");
        }
    }
}
