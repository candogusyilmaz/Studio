package cdy.studioapi.infrastructure;

import cdy.studioapi.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {
}