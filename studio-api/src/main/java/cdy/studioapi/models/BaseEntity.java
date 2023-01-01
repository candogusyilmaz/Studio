package cdy.studioapi.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    protected BaseEntity(int id) {
        this.id = id;
    }
}
