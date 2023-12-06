package dev.canverse.studio.api.features.slot.repositories;

import dev.canverse.studio.api.features.slot.entities.SlotItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotItemRepository extends JpaRepository<SlotItem, Integer>, JpaSpecificationExecutor<SlotItem> {
}