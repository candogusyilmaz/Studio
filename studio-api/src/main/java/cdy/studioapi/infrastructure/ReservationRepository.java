package cdy.studioapi.infrastructure;

import cdy.studioapi.events.ReservationActionCreateEvent;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.jpa.ReservationJpaRepository;
import cdy.studioapi.infrastructure.specs.ReservationSpecifications;
import cdy.studioapi.models.Reservation;
import cdy.studioapi.views.ReservationView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationRepository {
    private final ReservationJpaRepository reservationJpa;

    public Reservation save(Reservation reservation) {
        return reservationJpa.save(reservation);
    }

    public List<ReservationView> findAllAsReservationView() {
        return reservationJpa.findAllAsReservationView();
    }

    public List<ReservationView> findAllAsReservationView(int userId) {
        return reservationJpa.findAllAsReservationView(userId);
    }

    public boolean conflictingWithOthers(int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate);

        return reservationJpa.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithOthers(int reservationId, int slotId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithOthers(slotId, startDate, endDate)
                .and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationJpa.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate);

        return reservationJpa.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public boolean conflictingWithSelf(int reservationId, int userId, LocalDateTime startDate, LocalDateTime endDate) {
        var spec = ReservationSpecifications.conflictingWithSelf(userId, startDate, endDate)
                .and(ReservationSpecifications.reservationIdNotEqual(reservationId));

        return reservationJpa.findBy(spec, FluentQuery.FetchableFluentQuery::exists);
    }

    public Reservation getByIdIncludeUserSlot(int reservationId) throws NotFoundException {
        return reservationJpa.findBy(
                        (root, query, cb) -> cb.equal(root.get("id"), reservationId),
                        r -> r.project("slot", "user").first())
                .orElseThrow(() -> new NotFoundException("Rezervasyon bulunamadı."));
    }

    @EventListener
    public void updateLastActionWhenReservationActionCreated(ReservationActionCreateEvent event) {
        reservationJpa.updateLastAction(event.getAction().getReservation().getId(), event.getAction().getId());
    }
}
