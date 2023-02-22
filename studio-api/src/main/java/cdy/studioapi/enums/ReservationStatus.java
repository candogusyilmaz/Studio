package cdy.studioapi.enums;

public enum ReservationStatus {
    ACTIVE,
    CANCELLED,
    COMPLETED,
    CONFIRMED,
    PENDING,
    REJECTED,
    UPDATED;

    public boolean isCancellable() {
        return this == PENDING
                || this == UPDATED
                || this == ACTIVE
                || this == CONFIRMED;
    }
}
