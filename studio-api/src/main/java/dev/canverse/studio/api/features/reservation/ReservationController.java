package dev.canverse.studio.api.features.reservation;

import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import dev.canverse.studio.api.features.reservation.dtos.CreateReservation;
import dev.canverse.studio.api.features.reservation.dtos.ReservationInfo;
import dev.canverse.studio.api.features.reservation.dtos.UpdateReservation;
import dev.canverse.studio.api.features.reservation.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreateReservation.Request req) {
        reservationService.create(req);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateReservation.Request req, @PathVariable int id) {
        reservationService.update(id, req);
    }

    @GetMapping("/history")
    public Page<ReservationInfo> getReservationsHistoryByUser(@PageableDefault Pageable page) {
        return reservationService.findReservationsByUserId(authenticationProvider.getAuthentication().getId(), page);
    }

    @PatchMapping("/cancel/{id}")
    public void cancelReservation(@PathVariable int id) {
        reservationService.cancelReservation(id);
    }
}
