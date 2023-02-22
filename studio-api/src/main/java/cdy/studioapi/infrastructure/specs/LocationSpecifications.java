package cdy.studioapi.infrastructure.specs;

import cdy.studioapi.models.Location;
import org.springframework.data.jpa.domain.Specification;

public class LocationSpecifications {
    private LocationSpecifications() {
    }

    public static Specification<Location> findByName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), name);
    }
}
