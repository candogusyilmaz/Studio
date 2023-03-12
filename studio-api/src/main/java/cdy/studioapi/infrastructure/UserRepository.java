package cdy.studioapi.infrastructure;

import cdy.studioapi.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query("select u.tokenVersion from User u where u.username = :username")
    Optional<Integer> findTokenVersionByUsername(String username);

    @Query("select u from User u where u.username = :username")
    @EntityGraph(attributePaths = {"roles.permissions"})
    @Cacheable(value = "authentications", key = "#username", unless = "#result == null")
    User findByUsernameIncludePermissions(String username);
}
