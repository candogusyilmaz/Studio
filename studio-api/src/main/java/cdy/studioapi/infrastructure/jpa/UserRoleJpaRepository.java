package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {

    @Query("SELECT COUNT(rm.id) > 0 FROM UserRole rm WHERE rm.user.id = :userId and rm.role.id = :roleId")
    Boolean exists(Integer userId, Integer roleId);

    @Query("SELECT rm.id FROM UserRole rm WHERE rm.user.id = :userId and rm.role.id = :roleId")
    Optional<Integer> findIdByUserAndRole(Integer userId, Integer roleId);
}
