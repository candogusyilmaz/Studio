package dev.canverse.studio.api.features.room;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.location.repositories.LocationRepository;
import dev.canverse.studio.api.features.room.dtos.CreateRoom;
import dev.canverse.studio.api.features.room.dtos.RoomInfo;
import dev.canverse.studio.api.features.room.entities.Room;
import dev.canverse.studio.api.features.room.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public void create(CreateRoom.Request dto) {
        var location = Expect.of(locationRepository.findById(dto.getLocationId())).present("Location not found.");

        Expect.of(location.isRoot()).isFalse("Cannot create room at root location.");
        Expect.of(roomRepository.existsByName(dto.getName(), location.getId())).isFalse("Room name must be unique at location.");

        var room = new Room();
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setLocation(location);

        roomRepository.save(room);
    }

    public Page<RoomInfo> getAll(Pageable pageable) {
        return roomRepository.findBy((root, query, criteriaBuilder) -> null,
                r -> r.project("location").sortBy(pageable.getSort()).page(pageable).map(RoomInfo::new));
    }

    public RoomInfo getById(int id) {
        var room = roomRepository.findBy(
                (root, query, cb) -> cb.equal(root.get("id"), id),
                r -> Expect.of(r.project("location").first()).present("Room not found."));

        return new RoomInfo(room);
    }
}
