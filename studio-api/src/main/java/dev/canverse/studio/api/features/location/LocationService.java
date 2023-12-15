package dev.canverse.studio.api.features.location;

import dev.canverse.expectation.Expect;
import dev.canverse.studio.api.features.location.dtos.CreateLocation;
import dev.canverse.studio.api.features.location.dtos.LocationInfo;
import dev.canverse.studio.api.features.location.entities.Location;
import dev.canverse.studio.api.features.location.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    /**
     * Creates a new location based on the provided request.
     *
     * @param dto The request containing the details of the location to be created.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if a location with the same name already exists or if the parent location is not found.
     */
    public void create(CreateLocation.Request dto) {
        var locationAlreadyExists = false;

        Expect.of(locationRepository.exists(dto.getName(), dto.getParentId())).isFalse("There is already a location with the same name.");
        Expect.of(locationAlreadyExists).isFalse("There is already a location with the same name.");

        var location = new Location(dto.getName());

        Expect.of(dto.getParentId()).ifNotNull(id -> {
            var parent = Expect.of(locationRepository.findById(id)).present("Parent location not found.");
            location.setParent(parent);
        });

        locationRepository.save(location);
    }

    /**
     * Retrieves a list of all locations along with their rooms.
     *
     * @return A list of LocationInfo objects representing the locations.
     */
    public List<LocationInfo> getAll() {
        return locationRepository.findBy((root, query, criteriaBuilder) -> null,
                r -> r.project("rooms").all()).stream().map(LocationInfo::new).toList();
    }

    /**
     * Retrieves a paginated list of locations based on the provided specifications.
     *
     * @param pageable The pageable information specifying the page size, page number, and sorting.
     * @param specs    Specifications to filter the locations.
     * @return A Page containing LocationInfo objects representing the paginated locations.
     */
    @SafeVarargs
    public final Page<LocationInfo> getAll(Pageable pageable, Specification<Location>... specs) {
        return locationRepository.findBy(Specification.allOf(specs),
                r -> r.project("parent")
                        .sortBy(pageable.getSort()).page(pageable)).map(LocationInfo::new);
    }

    /**
     * Retrieves a LocationInfo object based on the provided location ID.
     *
     * @param id The ID of the location to retrieve.
     * @return A LocationInfo object representing the location.
     * @throws dev.canverse.expectation.ExpectationFailedException Thrown if the location with the specified ID is not found.
     */
    public LocationInfo getById(int id) {
        var location = locationRepository.findBy(
                (root, query, cb) -> cb.equal(root.get("id"), id),
                r -> Expect.of(r.project("parent").first()).present("Location not found."));

        return new LocationInfo(location);
    }
}
