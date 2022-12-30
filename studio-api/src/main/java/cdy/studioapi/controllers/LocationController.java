package cdy.studioapi.controllers;

import cdy.studioapi.dtos.LocationCreateDto;
import cdy.studioapi.services.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
