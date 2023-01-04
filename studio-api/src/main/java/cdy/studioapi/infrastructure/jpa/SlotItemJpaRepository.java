package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.SlotItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlotItemJpaRepository extends JpaRepository<SlotItem, Integer>, JpaSpecificationExecutor<SlotItem> {
}