package com.jkm.jimkanman.enums;

import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StorageReservationStatusTest {

    @Test
    void testValidTransitions() {
        assertTrue(StorageReservationStatus.PENDING.canTransitionTo(StorageReservationStatus.APPROVED));
        assertTrue(StorageReservationStatus.PENDING.canTransitionTo(StorageReservationStatus.REJECTED));
        assertTrue(StorageReservationStatus.APPROVED.canTransitionTo(StorageReservationStatus.STORING));
        assertTrue(StorageReservationStatus.STORING.canTransitionTo(StorageReservationStatus.COMPLETE));
    }

    @Test
    void testInvalidTransitions() {
        assertFalse(StorageReservationStatus.PENDING.canTransitionTo(StorageReservationStatus.COMPLETE));
        assertFalse(StorageReservationStatus.REJECTED.canTransitionTo(StorageReservationStatus.APPROVED));
        assertFalse(StorageReservationStatus.COMPLETE.canTransitionTo(StorageReservationStatus.PENDING));
    }
}
