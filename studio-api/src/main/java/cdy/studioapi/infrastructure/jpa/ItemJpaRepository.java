package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemJpaRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {
}