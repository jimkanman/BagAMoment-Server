package com.jkm.jimkanman.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryReservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_reservation_id")
    private StorageReservation storageReservation; // 보관소 예약과의 관계

    @OneToOne(mappedBy = "deliveryReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private LocalDateTime reservationDateTime; // 예약 날짜 및 시간
    private String deliveredLocation; // 도착 위치
}
