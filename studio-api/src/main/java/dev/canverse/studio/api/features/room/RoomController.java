package dev.canverse.studio.api.features.room;

import dev.canverse.studio.api.features.room.dtos.CreateRoom;
import dev.canverse.studio.api.features.room.dtos.RoomInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRoom(@RequestBody @Valid CreateRoom.Request req) {
        roomService.create(req);
    }

    @GetMapping
    public Page<RoomInfo> getRooms(@PageableDefault Pageable pageable) {
        return roomService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public RoomInfo getRoomById(@PathVariable int id) {
        return roomService.getById(id);
    }
}
