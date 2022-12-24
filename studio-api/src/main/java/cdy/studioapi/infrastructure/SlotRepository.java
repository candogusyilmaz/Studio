package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlotRepository extends JpaRepository<Slot, Integer>, JpaSpecificationExecutor<Slot> {
}