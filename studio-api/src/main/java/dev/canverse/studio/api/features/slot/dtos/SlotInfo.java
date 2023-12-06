package dev.canverse.studio.api.features.slot.dtos;

import dev.canverse.studio.api.features.item.dtos.ItemInfo;
import dev.canverse.studio.api.features.room.dtos.RoomInfo;
import dev.canverse.studio.api.features.slot.entities.Slot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SlotInfo implements Serializable {
    private int id;
    private String name;
    private RoomInfo room;
    private List<ItemInfo> items;

    public SlotInfo(Slot slot) {
        if (slot == null) return;

        this.id = slot.getId();
        this.name = slot.getName();
        this.room = new RoomInfo(slot.getRoom());
        this.items = slot.getSlotItems().stream().map(s -> new ItemInfo(s.getItem())).toList();
    }
}
