package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

public interface LocationJpaRepository extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {
    boolean existsByNameIgnoreCase(@NonNull String name);

}