package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocationRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {
}