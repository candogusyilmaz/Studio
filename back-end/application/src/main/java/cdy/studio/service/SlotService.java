package cdy.studio.service;

import cdy.studio.core.events.RoomCreated;
import cdy.studio.core.models.Slot;
import cdy.studio.infrastructure.repositories.SlotRepository;
import cdy.studio.infrastructure.specifications.SlotSpecifications;
import cdy.studio.service.requests.queries.AvailableSlotsQuery;
import cdy.studio.service.views.SlotView;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SlotService {
    private final SlotRepository repo;

    @EventListener
    private void createSlotsWhenRoomCreated(RoomCreated event) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 1; i <= event.getRoom().getCapacity(); i++) {
            var slot = new Slot();
            slot.setName("Masa " + i);
            slot.setRoom(event.getRoom());
            slots.add(slot);
        }

        repo.saveAll(slots);
    }

    public List<SlotView> getAll() {
        var slots = repo.findBy((root, query, cb) -> null, r -> r.sortBy(Sort.by("id")).project("room.location", "slotItems.item").all());
        return slots.stream().map(SlotView::new).toList();
    }

    public List<SlotView> findAvailableSlots(AvailableSlotsQuery criteria) {
        var spec = SlotSpecifications.availableSlots(criteria.getStartDate(), criteria.getEndDate());

        if (criteria.getRoomId() != null) {
            spec = spec.and(SlotSpecifications.roomIdEquals(criteria.getRoomId()));
        }

        var availableSlots = repo.findBy(spec,
                r -> r.sortBy(Sort.by("id"))
                        .project("room.location.parent", "slotItems.item").all());

        return availableSlots.stream().map(SlotView::new).toList();
    }
}
