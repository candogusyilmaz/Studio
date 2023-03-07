package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    @Query("select count(r.id) > 0 from Role r where r.name = :name")
    boolean exists(String name);
}
