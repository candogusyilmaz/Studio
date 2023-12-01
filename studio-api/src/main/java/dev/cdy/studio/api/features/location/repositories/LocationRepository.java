package dev.cdy.studio.api.features.location.repositories;

import dev.cdy.studio.api.features.location.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {
}