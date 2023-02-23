package cdy.studioapi.controllers;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.infrastructure.specs.LocationSpecifications;
import cdy.studioapi.services.LocationService;
import cdy.studioapi.views.LocationView;
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
    public void create(@RequestBody LocationCreateDto req) {
        locationService.create(req);
    }

    @GetMapping({"", "/"})
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
