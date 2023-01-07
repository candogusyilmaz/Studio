package cdy.studioapi.views;

import cdy.studioapi.models.Slot;
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

        if (Hibernate.isInitialized(slot.getItems())) {
            this.items = slot.getItems().stream().map(ItemView::new).toList();
        }
    }
}
