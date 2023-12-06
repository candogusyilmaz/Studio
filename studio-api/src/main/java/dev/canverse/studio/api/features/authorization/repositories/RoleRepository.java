package dev.canverse.studio.api.features.authorization.repositories;

import dev.canverse.studio.api.features.authorization.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
}
