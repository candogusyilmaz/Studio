package dev.canverse.studio.api.features.room;

import com.google.common.base.Preconditions;
import dev.canverse.studio.api.exceptions.NotFoundException;
import dev.canverse.studio.api.features.location.repositories.LocationRepository;
import dev.canverse.studio.api.features.room.dtos.CreateRoom;
import dev.canverse.studio.api.features.room.dtos.RoomInfo;
import dev.canverse.studio.api.features.room.entities.Room;
import dev.canverse.studio.api.features.room.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public void create(CreateRoom.Request dto) {
        var location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Lokasyon bulunamadı!"));

        Preconditions.checkArgument(roomNameUniqueAtLocation(location.getId(), dto.getName()),
                "Lokasyona ait aynı isimde bir oda bulunmaktadır.");

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
        var room = roomRepository.findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("location").first())
                .orElseThrow(() -> new NotFoundException("Oda bulunamadı."));

        return new RoomInfo(room);
    }

    private boolean roomNameUniqueAtLocation(int locationId, String roomName) {
        return roomRepository.findBy((root, query, cb) -> cb.and(
                cb.equal(cb.lower(root.get("name")), roomName.toLowerCase()),
                cb.equal(root.get("location").get("id"), locationId)
        ), FluentQuery.FetchableFluentQuery::exists);
    }
}
