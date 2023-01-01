package cdy.studioapi.views;

import cdy.studioapi.models.Slot;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class SlotView implements Serializable {
    private final int id;
    private final String name;
    private final RoomView room;
    private final List<ItemView> items;

    public SlotView(Slot slot) {
        this.id = slot.getId();
        this.name = slot.getName();
        this.room = new RoomView(slot.getRoom());
        this.items = slot.getItems().stream().map(ItemView::new).toList();
    }
}
