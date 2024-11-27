package com.jkm.jimkanman.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_reservation")
@Builder
public class DeliveryReservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_reservation_id")
    private StorageReservation storageReservation; // 보관소 예약과의 관계

    @Setter
    @OneToOne(mappedBy = "deliveryReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    private LocalDateTime deliveryArrivalDateTime; // 예약 날짜 및 시간
    private String destinationAddress; // 목적지
    private String destinationPostalCode; // 우편번호
    private Double destinationLatitude;
    private Double destinationLongitude;

    public void setStorageReservation(StorageReservation storageReservation) {
        if(storageReservation == null) return;
        if(this.storageReservation != null) this.storageReservation.setDeliveryReservation(null);
        this.storageReservation = storageReservation;
        storageReservation.setDeliveryReservation(this);
    }
}
