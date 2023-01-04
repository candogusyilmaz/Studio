package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String roleName);

    @Query("select count(r.id) > 0 from Role r where r.name = :name")
    boolean exists(String name);
}
