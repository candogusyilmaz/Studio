package cdy.studioapi.services;

import cdy.studioapi.dtos.RoomCreateDto;
import cdy.studioapi.events.RoomCreateEvent;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.infrastructure.RoomRepository;
import cdy.studioapi.models.Room;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
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
}
