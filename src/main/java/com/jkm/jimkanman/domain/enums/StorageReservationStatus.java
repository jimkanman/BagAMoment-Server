package com.jkm.jimkanman.domain.enums;

public enum StorageReservationStatus {
    REJECTED, PENDING, APPROVED, STORING, COMPLETE;

    public boolean canTransitionTo(StorageReservationStatus next) {
        if(next == null) return false;
        return switch (this) {
            case PENDING -> next == REJECTED || next == APPROVED;
            case APPROVED -> next == STORING;
            case STORING -> next == COMPLETE;
            default -> false;
        };
    }
}
