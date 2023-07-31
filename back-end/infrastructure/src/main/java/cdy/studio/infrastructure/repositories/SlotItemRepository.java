package cdy.studio.infrastructure.repositories;

import cdy.studio.core.models.SlotItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotItemRepository extends JpaRepository<SlotItem, Integer>, JpaSpecificationExecutor<SlotItem> {
}