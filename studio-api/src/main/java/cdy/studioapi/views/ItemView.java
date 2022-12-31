package cdy.studioapi.views;

import cdy.studioapi.models.Item;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ItemView implements Serializable {
    private final int id;
    private final String name;

    public ItemView(Item item) {
        this.id = item.getId();
        this.name = item.getName();
    }
}