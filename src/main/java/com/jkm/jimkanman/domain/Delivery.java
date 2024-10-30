package com.jkm.jimkanman.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_reservation_id")
    private DeliveryReservation deliveryReservation; // 배송 예약과의 관계

    private boolean isStarted;
    private Double latitude;
    private Double longitude;
    private LocalDateTime arrivalTime;
    private String deliveredLocation; // 실제로 배송 완료한 지점
}
