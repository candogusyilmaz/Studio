package dev.canverse.studio.api.features.shared;

import dev.canverse.expectation.Expect;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
@Getter
public class TimePeriod implements Serializable {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    protected TimePeriod() {
    }

    public TimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        Expect.of(startDate).before(endDate, "Start date must be before end date.");
        this.startDate = startDate.withSecond(59).truncatedTo(ChronoUnit.SECONDS);
        this.endDate = endDate.withSecond(1).truncatedTo(ChronoUnit.SECONDS);
    }
}
