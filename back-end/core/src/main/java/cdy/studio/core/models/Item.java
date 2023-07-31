package cdy.studio.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String pictureUrl;
}
