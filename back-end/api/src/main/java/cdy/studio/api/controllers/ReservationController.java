package cdy.studio.api.controllers;

import cdy.studio.service.AuthenticationProvider;
import cdy.studio.service.ReservationService;
import cdy.studio.service.exceptions.NotFoundException;
import cdy.studio.service.requests.ReservationCreateRequest;
import cdy.studio.service.requests.ReservationUpdateRequest;
import cdy.studio.service.views.ReservationView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ReservationCreateRequest req) {
        reservationService.create(req, authenticationProvider.getAuthentication().getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    public Set<ReservationView> getReservations() {
        return reservationService.getAll();
    }

    @PatchMapping("/cancel/{id}")
    public void cancelReservation(@PathVariable int id) {
        reservationService.cancelReservation(id);
    }
}
