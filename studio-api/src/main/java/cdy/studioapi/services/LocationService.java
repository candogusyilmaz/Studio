package cdy.studioapi.services;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.models.Location;
import cdy.studioapi.views.LocationView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void create(LocationCreateDto dto) {
        var nameExists = locationRepository.existsByNameIgnoreCase(dto.name());

        if (nameExists) {
            throw new BadRequestException("There is already a location with the same name.");
        }

        var location = new Location(dto.name());

        dto.parentId().ifPresent(s ->
                location.setParent(locationRepository
                        .findById(s)
                        .orElseThrow(() -> new BadRequestException("Parent is not found")))
        );

        locationRepository.save(location);
    }

    public List<LocationView> getAll() {
        return locationRepository.findBy((root, query, cb) -> null, r -> r.project("parent").all()
                .stream().map(LocationView::new).toList());
    }

    public LocationView getById(int id) {
        return new LocationView(locationRepository.findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("parent").first()
                .orElseThrow(() -> new NotFoundException("Lokasyon bulunamadı."))));
    }
}
