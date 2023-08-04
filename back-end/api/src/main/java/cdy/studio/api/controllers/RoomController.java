package cdy.studio.api.controllers;

import cdy.studio.service.RoomService;
import cdy.studio.service.requests.RoomCreateRequest;
import cdy.studio.service.views.RoomView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RoomCreateRequest req) {
        roomService.create(req);
    }

    @GetMapping
    public Page<RoomView> getAll(@PageableDefault Pageable pageable) {
        return roomService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public RoomView getById(@PathVariable int id) {
        return roomService.getById(id);
    }
}
