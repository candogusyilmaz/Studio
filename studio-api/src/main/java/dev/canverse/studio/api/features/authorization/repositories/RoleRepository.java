package dev.canverse.studio.api.features.authorization.repositories;

import dev.canverse.studio.api.features.authorization.entities.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);

    @EntityGraph(attributePaths = "rolePermissions")
    Optional<Role> findByName(String name);
}
