package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.Slot;
import cdy.studioapi.views.SlotView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlotJpaRepository extends JpaRepository<Slot, Integer>, JpaSpecificationExecutor<Slot> {

    @Query("select s from Slot s order by s.id asc")
    @EntityGraph(attributePaths = {"room", "items"})
    List<SlotView> findAllIncludeRoomsItems();
}
