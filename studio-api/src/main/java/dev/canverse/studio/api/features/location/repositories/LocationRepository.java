package dev.canverse.studio.api.features.location.repositories;

import dev.canverse.studio.api.features.location.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {

    @Query("SELECT COUNT(l.id) > 0 FROM Location l WHERE " +
            "CASE WHEN :parentId IS NULL " +
            "THEN (lower(l.name) = lower(:name)) " +
            "ELSE (lower(l.name) = lower(:name) AND l.parent.id = :parentId) END")
    boolean exists(String name, Integer parentId);
}