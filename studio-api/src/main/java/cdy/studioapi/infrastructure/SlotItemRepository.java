package cdy.studioapi.infrastructure;

import cdy.studioapi.models.SlotItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlotItemRepository extends JpaRepository<SlotItem, Integer>, JpaSpecificationExecutor<SlotItem> {
}