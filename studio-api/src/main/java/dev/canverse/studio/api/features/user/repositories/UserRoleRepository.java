package dev.canverse.studio.api.features.user.repositories;

import dev.canverse.studio.api.features.user.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {
    @Query("select count(ur) > 0 from UserRole ur where ur.user.id = :userId and ur.role.id = :roleId")
    boolean existsBy(Integer userId, Integer roleId);
}
