package cdy.studioapi.models;

import cdy.studioapi.enums.QuoteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "quotes")
public class Quote extends AuditableEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
    @Column(nullable = false, updatable = false)
    private String content;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private QuoteStatus status = QuoteStatus.PENDING;
    @Setter(AccessLevel.NONE)
    private LocalDate lastShownDate;
    @Setter(AccessLevel.NONE)
    private LocalDate statusResetDate;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private int shownTimes = 0;
    @Column(nullable = false)
    private boolean enabled;

    public void setActive() {
        lastShownDate = LocalDate.now();
        shownTimes = shownTimes + 1;
        this.status = QuoteStatus.ACTIVE;
    }

    public void setShown() {
        statusResetDate = LocalDate.now().plusMonths(1);
        this.status = QuoteStatus.SHOWN;
    }

    public void setPending() {
        statusResetDate = null;
        this.status = QuoteStatus.PENDING;
    }
}
