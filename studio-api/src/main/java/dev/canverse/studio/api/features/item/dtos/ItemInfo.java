package dev.canverse.studio.api.features.item.dtos;

import dev.canverse.studio.api.features.item.entities.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ItemInfo implements Serializable {
    private int id;
    private String name;

    public ItemInfo(Item item) {
        if (item == null) return;

        this.id = item.getId();
        this.name = item.getName();
    }
}