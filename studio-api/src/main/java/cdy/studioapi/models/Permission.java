package cdy.studioapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String displayName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    protected Permission() {
    }

    public Permission(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = StringUtils.normalizeSpace(displayName);
    }

    @PreUpdate
    @PrePersist
    private void upsertPreChecks() {
        if (StringUtils.isBlank(displayName)) {
            throw new IllegalArgumentException("İzin adı boş olamaz.");
        }
    }
}
