package cdy.studio.service;

import cdy.studio.core.models.Location;
import cdy.studio.infrastructure.repositories.LocationRepository;
import cdy.studio.infrastructure.specifications.LocationSpecifications;
import cdy.studio.service.exceptions.BadRequestException;
import cdy.studio.service.exceptions.NotFoundException;
import cdy.studio.service.requests.LocationCreateRequest;
import cdy.studio.service.views.LocationView;
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

    public void create(LocationCreateRequest dto) {
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

        if (locationAlreadyExists) throw new BadRequestException("Aynı isimde lokasyon zaten var.");

        var location = new Location(dto.getName());

        if (dto.getParentId() != null) {
            var parent = locationRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Üst lokasyon bulunamadı."));

            location.setParent(parent);
        }

        locationRepository.save(location);
    }

    public List<LocationView> getAll() {
        return locationRepository.findBy((root, query, criteriaBuilder) -> null,
                r -> r.project("parent").all()).stream().map(LocationView::new).toList();
    }

    @SafeVarargs
    public final Page<LocationView> getAll(Pageable pageable, Specification<Location>... specs) {
        return locationRepository.findBy(Specification.allOf(specs),
                r -> r.project("parent")
                        .sortBy(pageable.getSort()).page(pageable)).map(LocationView::new);
    }

    public LocationView getById(int id) {
        return new LocationView(locationRepository.findBy((root, query, cb) -> cb.equal(root.get("id"), id), r -> r.project("parent").first()
                .orElseThrow(() -> new NotFoundException(Location.class, id))));
    }
}
