package dev.canverse.studio.api.features.slot.repositories;

import dev.canverse.studio.api.features.slot.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer>, JpaSpecificationExecutor<Slot> {
}
