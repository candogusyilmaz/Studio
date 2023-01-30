package cdy.studioapi.services;

import cdy.studioapi.dtos.RoomCreateDto;
import cdy.studioapi.events.RoomCreateEvent;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.infrastructure.jpa.RoomJpaRepository;
import cdy.studioapi.models.Room;
import cdy.studioapi.views.RoomView;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.FluentQuery;
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

        if (roomNameUniqueAtLocation(location.getId(), dto.getName())) {
            throw new BadRequestException("Lokasyona ait aynı isimde bir oda bulunmaktadır.");
        }

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

    public RoomView getById(int id) {
        var room = roomRepository.findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("location").first())
                .orElseThrow(() -> new NotFoundException("Oda bulunamadı."));

        return new RoomView(room);
    }

    private boolean roomNameUniqueAtLocation(int locationId, String roomName) {
        return roomRepository.findBy((root, query, cb) -> cb.and(
                cb.equal(cb.lower(root.get("name")), roomName.toLowerCase()),
                cb.equal(root.get("location").get("id"), locationId)
        ), FluentQuery.FetchableFluentQuery::exists);
    }
}
