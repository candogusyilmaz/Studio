package dev.canverse.studio.api.features.location;

import com.google.common.base.Preconditions;
import dev.canverse.studio.api.exceptions.NotFoundException;
import dev.canverse.studio.api.features.location.dtos.CreateLocation;
import dev.canverse.studio.api.features.location.dtos.LocationInfo;
import dev.canverse.studio.api.features.location.entities.Location;
import dev.canverse.studio.api.features.location.repositories.LocationRepository;
import dev.canverse.studio.api.features.location.repositories.LocationSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void create(CreateLocation.Request dto) {
        var locationAlreadyExists = false;

        if (dto.getParentId() != null) {
            locationAlreadyExists = locationRepository.findBy(
                    LocationSpecifications.findByNameAndParentId(dto.getName(), dto.getParentId()),
                    FluentQuery.FetchableFluentQuery::exists);
        } else {
            locationAlreadyExists = locationRepository.findBy(
                    LocationSpecifications.findByName(dto.getName()),
                    FluentQuery.FetchableFluentQuery::exists);
        }

        Preconditions.checkArgument(!locationAlreadyExists, "Aynı isimde lokasyon zaten var.");

        var location = new Location(dto.getName());

        if (dto.getParentId() != null) {
            var parent = locationRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Üst lokasyon bulunamadı."));

            location.setParent(parent);
        }

        locationRepository.save(location);
    }

    public List<LocationInfo> getAll() {
        return locationRepository.findBy((root, query, criteriaBuilder) -> null,
                r -> r.project("rooms").all()).stream().map(LocationInfo::new).toList();
    }

    @SafeVarargs
    public final Page<LocationInfo> getAll(Pageable pageable, Specification<Location>... specs) {
        return locationRepository.findBy(Specification.allOf(specs),
                r -> r.project("parent")
                        .sortBy(pageable.getSort()).page(pageable)).map(LocationInfo::new);
    }

    public LocationInfo getById(int id) {
        var location = locationRepository
                .findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("parent").first()
                        .orElseThrow(() -> new NotFoundException("Location with the given id not found.", id)));

        return new LocationInfo(location);
    }
}
