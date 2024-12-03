package com.jkm.jimkanman.repository;

import com.jkm.jimkanman.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByOrderByCreatedAtDesc();
}
