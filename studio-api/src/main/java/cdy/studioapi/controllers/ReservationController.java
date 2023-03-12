package cdy.studioapi.controllers;

import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.requests.ReservationCreateRequest;
import cdy.studioapi.requests.ReservationUpdateRequest;
import cdy.studioapi.services.AuthenticationProvider;
import cdy.studioapi.services.ReservationService;
import cdy.studioapi.views.ReservationView;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void create(@RequestBody @Valid ReservationCreateRequest req) {
        reservationService.create(req, authenticationProvider.getAuthentication().getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid ReservationUpdateRequest req, @PathVariable int id) {
        if (id <= 0) {
            throw new NotFoundException("Rezervasyon bulunamadı.");
        }

        reservationService.update(id, req);
    }

    @GetMapping("/history")
    public Page<ReservationView> getReservationsHistoryByUser(@PageableDefault Pageable page) {
        return reservationService.getAll(authenticationProvider.getAuthentication().getId(), page);
    }

    @GetMapping("/all")
    public List<ReservationView> getReservations() {
        return reservationService.getAll();
    }

    @PatchMapping("/cancel/{id}")
    @Transactional
    public void cancelReservation(@PathVariable int id) {
        reservationService.cancelReservation(id);
    }
}
