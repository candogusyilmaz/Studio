package cdy.studio.service.views;

import cdy.studio.core.models.Slot;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.List;

@Getter
public class SlotView implements Serializable {
    private final int id;
    private final String name;
    private RoomView room;
    private List<ItemView> items;

    public SlotView(Slot slot) {
        this.id = slot.getId();
        this.name = slot.getName();

        if (Hibernate.isInitialized(slot.getRoom())) {
            this.room = new RoomView(slot.getRoom());
        }

        if (Hibernate.isInitialized(slot.getSlotItems())) {
            this.items = slot.getSlotItems().stream().filter(s -> Hibernate.isInitialized(s.getItem())).map(s -> new ItemView(s.getItem())).toList();
        }
    }
}