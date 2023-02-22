package cdy.studioapi.services;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.models.Location;
import cdy.studioapi.views.LocationView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void create(LocationCreateDto dto) {
        if (locationRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("There is already a location with the same name.");
        }

        var location = new Location(dto.getName());
        
        dto.getParentId()
                .map(locationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .stream().findFirst()
                .ifPresent(location::setParent);

        locationRepository.save(location);
    }

    public Page<LocationView> getAll(Pageable pageable, Specification<Location>... specs) {
        return locationRepository.findBy(Specification.allOf(specs),
                r -> r.project("parent")
                        .sortBy(pageable.getSort()).page(pageable)).map(LocationView::new);
    }

    public LocationView getById(int id) {
        return new LocationView(locationRepository.findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("parent").first()
                .orElseThrow(() -> new NotFoundException("Lokasyon bulunamadı."))));
    }
}
