package dev.cdy.studio.api.features.user.repositories;

import dev.cdy.studio.api.features.user.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {
}
