package cdy.studioapi.services;

import cdy.studioapi.dtos.RoomCreateDto;
import cdy.studioapi.events.RoomCreateEvent;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.infrastructure.jpa.RoomJpaRepository;
import cdy.studioapi.models.Room;
import cdy.studioapi.views.RoomView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomJpaRepository roomRepository;
    private final LocationRepository locationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void create(RoomCreateDto dto) {
        var location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Lokasyon bulunamadı!"));

        var room = new Room();
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setLocation(location);

        roomRepository.save(room);
        eventPublisher.publishEvent(new RoomCreateEvent(room));
    }

    public List<RoomView> getAll() {
        return roomRepository.findBy((root, query, criteriaBuilder) -> null, r -> r.project("location").all().stream().map(RoomView::new).toList());
    }
}
