package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

}