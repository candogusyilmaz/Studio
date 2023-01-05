package cdy.studioapi.controllers;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.services.LocationService;
import cdy.studioapi.views.LocationView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<LocationView> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{id}")
    public LocationView getById(@PathVariable int id) {
        return locationService.getById(id);
    }
}
