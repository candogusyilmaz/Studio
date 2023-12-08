package dev.canverse.studio.api.features.location;

import dev.canverse.studio.api.features.location.dtos.CreateLocation;
import dev.canverse.studio.api.features.location.dtos.LocationInfo;
import dev.canverse.studio.api.features.location.repositories.LocationSpecifications;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreateLocation.Request req) {
        locationService.create(req);
    }

    @GetMapping
    public Page<LocationInfo> getAll(@PageableDefault Pageable pageable, Optional<String> name) {
        var nameQuerySpec = name.map(LocationSpecifications::findByName).orElse(null);
        return locationService.getAll(pageable, nameQuerySpec);
    }

    @GetMapping("/all")
    public List<LocationInfo> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{id}")
    public LocationInfo getById(@PathVariable int id) {
        return locationService.getById(id);
    }
}
