package cdy.studioapi.controllers;

import cdy.studioapi.config.Auth;
import cdy.studioapi.dtos.ReservationCreateDto;
import cdy.studioapi.dtos.ReservationUpdateDto;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.services.ReservationService;
import cdy.studioapi.views.ReservationView;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void create(@RequestBody @Valid ReservationCreateDto req, Auth auth) {
        reservationService.create(req, auth.getUser().getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid ReservationUpdateDto req, @PathVariable int id) {
        if (id <= 0) {
            throw new NotFoundException("Rezervasyon bulunamadı.");
        }

        reservationService.update(id, req);
    }

    @GetMapping({"", "/"})
    public List<ReservationView> getReservationsByAuth(Auth auth) {
        return reservationService.getAll(auth.getUser().getId());
    }

    @GetMapping("/all")
    public List<ReservationView> getReservations() {
        return reservationService.getAll();
    }
}
