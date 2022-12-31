package cdy.studioapi.controllers;

import cdy.studioapi.dtos.RoomCreateDto;
import cdy.studioapi.services.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void create(@RequestBody @Valid RoomCreateDto req) {
        roomService.create(req);
    }
}
