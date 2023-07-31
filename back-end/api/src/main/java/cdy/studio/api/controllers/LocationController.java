package cdy.studio.api.controllers;

import cdy.studio.infrastructure.specifications.LocationSpecifications;
import cdy.studio.service.requests.LocationCreateRequest;
import cdy.studio.service.LocationService;
import cdy.studio.service.views.LocationView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LocationCreateRequest req) {
        locationService.create(req);
    }

    @GetMapping
    public Page<LocationView> getAll(@PageableDefault Pageable pageable, Optional<String> name) {
        var nameQuerySpec = name.map(LocationSpecifications::findByName).orElse(null);
        return locationService.getAll(pageable, nameQuerySpec);
    }

    @GetMapping("/all")
    public List<LocationView> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{id}")
    public LocationView getById(@PathVariable int id) {
        return locationService.getById(id);
    }
}
