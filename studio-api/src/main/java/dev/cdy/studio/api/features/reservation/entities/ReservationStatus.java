package dev.cdy.studio.api.features.reservation.entities;

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
