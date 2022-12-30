package cdy.studioapi.services;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.enums.ExceptionCode;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.infrastructure.LocationRepository;
import cdy.studioapi.models.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void create(LocationCreateDto dto) {
        var nameExists = locationRepository.existsByNameIgnoreCase(dto.name());

        if (nameExists) {
            throw new BadRequestException(ExceptionCode.LOCATION_NOT_UNIQUE, "There is already a location with the same name.");
        }

        var location = new Location(dto.name());

        dto.parentId().ifPresent(s -> {
            location.setParent(locationRepository
                    .findById(s)
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.PARENT_NOT_FOUND, "Parent is not found")));
        });

        locationRepository.save(location);
    }
}
