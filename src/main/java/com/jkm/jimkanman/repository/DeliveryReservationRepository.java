package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.DeliveryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryReservationRepository extends JpaRepository<DeliveryReservation, Long> {
    List<DeliveryReservation> findAllByOrderByCreatedAtDesc();

    @Query("""
        SELECT dr
        FROM DeliveryReservation dr
        JOIN FETCH dr.delivery d
        JOIN FETCH dr.storageReservation sr
        ORDER BY dr.createdAt DESC
    """)
    List<DeliveryReservation> findAllWithDeliveryAndStorageOrderByCreatedAtDesc();
}
