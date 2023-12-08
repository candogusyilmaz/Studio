package dev.canverse.studio.api.features.slot.services;

import dev.canverse.studio.api.features.room.events.RoomCreatedEvent;
import dev.canverse.studio.api.features.slot.dtos.AvailableSlots;
import dev.canverse.studio.api.features.slot.dtos.SlotInfo;
import dev.canverse.studio.api.features.slot.entities.Slot;
import dev.canverse.studio.api.features.slot.repositories.SlotRepository;
import dev.canverse.studio.api.features.slot.repositories.SlotSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {
    private final SlotRepository repo;

    @EventListener
    private void createSlotsWhenRoomCreated(RoomCreatedEvent event) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 1; i <= event.getRoom().getCapacity(); i++) {
            var slot = new Slot();
            slot.setName("Masa " + i);
            slot.setRoom(event.getRoom());
            slots.add(slot);
        }

        repo.saveAll(slots);
    }

    public List<SlotInfo> getAll() {
        var slots = repo.findBy((root, query, cb) -> null, r -> r.sortBy(Sort.by("id")).project("room.location", "slotItems.item").all());
        return slots.stream().map(SlotInfo::new).toList();
    }

    public List<AvailableSlots.Response> findAvailableSlots(AvailableSlots.Request req) {
        var spec = SlotSpecifications.availableSlots(req.getStartDate(), req.getEndDate());

        if (req.getRoomId() != null) {
            spec = spec.and(SlotSpecifications.roomIdEquals(req.getRoomId()));
        }

        var availableSlots = repo.findBy(spec,
                r -> r.sortBy(Sort.by("id"))
                        .project("room.location.parent", "slotItems.item").all());

        return availableSlots.stream().map(AvailableSlots.Response::new).toList();
    }
}
