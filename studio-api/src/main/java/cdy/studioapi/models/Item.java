package cdy.studioapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String pictureUrl;
}
