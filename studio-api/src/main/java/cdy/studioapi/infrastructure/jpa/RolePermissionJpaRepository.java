package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {
}