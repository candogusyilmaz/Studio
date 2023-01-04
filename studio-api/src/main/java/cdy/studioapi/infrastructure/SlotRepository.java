package cdy.studioapi.infrastructure;

import cdy.studioapi.dtos.queries.SlotCriteria;
import cdy.studioapi.exceptions.NotFoundException;
import cdy.studioapi.infrastructure.jpa.SlotJpaRepository;
import cdy.studioapi.infrastructure.specs.SlotSpecifications;
import cdy.studioapi.models.Slot;
import cdy.studioapi.views.SlotView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SlotRepository {
    private final SlotJpaRepository slotJpa;

    public List<Slot> saveAll(List<Slot> slot) {
        return slotJpa.saveAll(slot);
    }

    public Slot getById(int id) throws NotFoundException {
        return slotJpa.findBy((root, query, cb) -> cb.equal(root.get("id"), id), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new NotFoundException("Slot bulunamadı."));
    }

    public List<SlotView> findAllIncludeRoomsItems() {
        return slotJpa.findAllIncludeRoomsItems();
    }

    public List<SlotView> findAvailableSlots(SlotCriteria criteria) {
        var spec = SlotSpecifications.availableSlots(criteria.getStartDate(), criteria.getEndDate());

        if (criteria.getRoomId() != null) {
            spec = spec.and(SlotSpecifications.roomIdEquals(criteria.getRoomId()));
        }

        var availableSlots = slotJpa.findBy(spec,
                r -> r.sortBy(Sort.by("id"))
                        .project("room", "items").all());

        return availableSlots.stream().map(SlotView::new).toList();
    }
}
