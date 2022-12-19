package cdy.studioapi.infrastructure;

import cdy.studioapi.dtos.RoleDto;
import cdy.studioapi.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String roleName);

    @Query("select new cdy.studioapi.dtos.RoleDto(r.id, r.name) from Role r")
    Page<RoleDto> getAllPaged(Pageable pageable);

    @Query("select count(r.id) > 0 from Role r where r.name = :name")
    boolean exists(String name);
}
