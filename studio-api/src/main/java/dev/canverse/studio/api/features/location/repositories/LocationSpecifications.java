package dev.canverse.studio.api.features.location.repositories;

import dev.canverse.studio.api.features.location.entities.Location;
import org.springframework.data.jpa.domain.Specification;

public class LocationSpecifications {
    private LocationSpecifications() {
    }

    public static Specification<Location> findByName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<Location> findByNameAndParentId(String name, int parentId) {
        return (root, query, cb) -> cb.and(
                cb.like(cb.lower(root.get("name")), name.toLowerCase()),
                cb.equal(root.get("parent").get("id"), parentId)
        );
    }
}
