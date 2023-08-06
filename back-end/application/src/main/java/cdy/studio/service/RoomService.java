package cdy.studio.service;

import cdy.studio.core.models.Room;
import cdy.studio.infrastructure.repositories.LocationRepository;
import cdy.studio.infrastructure.repositories.RoomRepository;
import cdy.studio.service.exceptions.BadRequestException;
import cdy.studio.service.exceptions.NotFoundException;
import cdy.studio.service.requests.RoomCreateRequest;
import cdy.studio.service.views.RoomView;
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
    public void create(RoomCreateRequest dto) {
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
    }

    public Page<RoomView> getAll(Pageable pageable) {
        return roomRepository.findBy((root, query, criteriaBuilder) -> null,
                r -> r.project("location").sortBy(pageable.getSort()).page(pageable).map(RoomView::new));
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
