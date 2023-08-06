package cdy.studio.service.views;

import cdy.studio.core.enums.QuoteStatus;
import cdy.studio.core.models.Quote;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class QuoteView implements Serializable {
    private final int id;
    private final String content;
    private final QuoteStatus status;
    private final LocalDate lastShownDate;
    private final LocalDate statusResetDate;
    private final int shownTimes;
    private final boolean enabled;
    private UserBasicView user;

    public QuoteView(Quote content) {
        this.id = content.getId();
        this.content = content.getContent();
        this.status = content.getStatus();
        this.lastShownDate = content.getLastShownDate();
        this.statusResetDate = content.getStatusResetDate();
        this.shownTimes = content.getShownTimes();
        this.enabled = content.isEnabled();

        if (Hibernate.isInitialized(content.getUser())) {
            this.user = new UserBasicView(content.getUser());
        }
    }
}
