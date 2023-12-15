package dev.canverse.studio.api.features.authorization.repositories;


import dev.canverse.studio.api.features.authorization.entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {
    @Query("SELECT COUNT(rp) > 0 FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    boolean exists(int roleId, int permissionId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    Optional<RolePermission> findById(int roleId, int permissionId);
}