package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {
    boolean existsByNameIgnoreCase(@NonNull String name);
}