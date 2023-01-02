package cdy.studioapi.services;

import cdy.studioapi.dtos.ReservationCreateDto;
import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.events.ReservationCreateEvent;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.ReservationRepository;
import cdy.studioapi.infrastructure.SlotRepository;
import cdy.studioapi.models.Reservation;
import cdy.studioapi.models.User;
import cdy.studioapi.views.ReservationView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void create(ReservationCreateDto dto, int userId) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Başlangıç tarihi bitiş tarihinden sonra olamaz.");
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
