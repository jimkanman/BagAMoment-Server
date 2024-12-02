package com.jkm.jimkanman.domain;

import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "delivery")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_reservation_id")
    private DeliveryReservation deliveryReservation; // 배송 예약과의 관계

//    @Setter
//    @Column(nullable = false)
//    @ColumnDefault("'PENDING'")
//    @Enumerated(EnumType.STRING)
//    private DeliveryStatus status;

    @Setter
    private Double latitude;
    @Setter
    private Double longitude;
    private LocalDateTime arrivalTime;

    public void setDeliveryReservation(DeliveryReservation deliveryReservation) {
        if(deliveryReservation == null) return;
        if(this.deliveryReservation != null) this.deliveryReservation.setDelivery(null);
        this.deliveryReservation = deliveryReservation;
        deliveryReservation.setDelivery(this);
    }
}
