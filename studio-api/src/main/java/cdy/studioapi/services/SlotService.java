package cdy.studioapi.services;

import cdy.studioapi.dtos.queries.SlotCriteria;
import cdy.studioapi.events.RoomCreateEvent;
import cdy.studioapi.infrastructure.SlotRepository;
import cdy.studioapi.infrastructure.specs.SlotSpecifications;
import cdy.studioapi.models.Slot;
import cdy.studioapi.views.SlotView;
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
    public void createSlotsWhenRoomCreated(RoomCreateEvent event) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 1; i <= event.getRoom().getCapacity(); i++) {
            var slot = new Slot();
            slot.setName("Slot #" + i);
            slot.setRoom(event.getRoom());
            slots.add(slot);
        }

        repo.saveAll(slots);
    }

    public List<SlotView> getAll() {
        return repo.findAllIncludeRoomsItems();
    }

    public List<SlotView> findAvailableSlots(SlotCriteria criteria) {
        var spec = SlotSpecifications.availableSlots(criteria.getStartDate(), criteria.getEndDate());

        if (criteria.getRoomId() != null) {
            spec = spec.and(SlotSpecifications.roomIdEquals(criteria.getRoomId()));
        }

        var availableSlots = repo.findBy(spec,
                r -> r.sortBy(Sort.by("id"))
                        .project("room", "items").all());

        return availableSlots.stream().map(SlotView::new).toList();
    }
}
