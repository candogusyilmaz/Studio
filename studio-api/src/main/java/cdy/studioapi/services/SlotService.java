package cdy.studioapi.services;

import cdy.studioapi.dtos.queries.SlotCriteria;
import cdy.studioapi.events.RoomCreateEvent;
import cdy.studioapi.infrastructure.SlotRepository;
import cdy.studioapi.models.Slot;
import cdy.studioapi.views.SlotView;
import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;

    @EventListener
    public void createSlotsWhenRoomCreated(RoomCreateEvent event) {
        List<Slot> slots = new ArrayList<>();

        for (int i = 1; i <= event.getRoom().getCapacity(); i++) {
            var slot = new Slot();
            slot.setName("Slot #" + i);
            slot.setRoom(event.getRoom());
            slots.add(slot);
        }

        slotRepository.saveAll(slots);
    }

    public List<SlotView> getAll() {
        return slotRepository.findAllIncludeRoomsItems();
    }

    public List<SlotView> findAvailableSlots(SlotCriteria criteria) {

        Specification<Slot> spec = Specification.where((root, query, cb) -> {
            var res = root.join("reservations", JoinType.LEFT);

            res.on(
                    cb.and(
                            cb.lessThanOrEqualTo(res.get("startDate"), criteria.getEndDate()),
                            cb.greaterThanOrEqualTo(res.get("endDate"), criteria.getStartDate())
                    )
            );

            return cb.and(
                    cb.isNull(res.get("slot").get("id"))
            );
        });

        if (criteria.getRoomId() != null) {
            spec = spec.and((root, query, cb) -> {
                var room = root.join("room", JoinType.LEFT);
                return cb.equal(room.get("id"), criteria.getRoomId());
            });
        }

        var availableSlots = slotRepository.findBy(spec,
                r -> r
                        .sortBy(Sort.by("id"))
                        .project("room", "items").all());

        return availableSlots.stream().map(SlotView::new).toList();
    }
}
