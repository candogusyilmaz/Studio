package dev.cdy.studio.api.features.quote.dtos;

import dev.cdy.studio.api.features.quote.entities.Quote;
import dev.cdy.studio.api.features.quote.entities.QuoteStatus;
import dev.cdy.studio.api.features.user.dtos.UserSummaryInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class QuoteInfo implements Serializable {
    private int id;
    private String content;
    private QuoteStatus status;
    private LocalDate lastShownDate;
    private LocalDate statusResetDate;
    private int shownTimes;
    private boolean enabled;
    private UserSummaryInfo user;

    public QuoteInfo(Quote content) {
        if (content == null) return;

        this.id = content.getId();
        this.content = content.getContent();
        this.status = content.getStatus();
        this.lastShownDate = content.getLastShownDate();
        this.statusResetDate = content.getStatusResetDate();
        this.shownTimes = content.getShownTimes();
        this.enabled = content.isEnabled();
        this.user = new UserSummaryInfo(content.getUser());
    }
}
