package com.jkm.jimkanman.domain;

import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "storage_reservation")
@Getter
public class StorageReservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원과의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage; // 보관소와의 관계

    @OneToOne(mappedBy = "storageReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeliveryReservation deliveryReservation;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Luggage> luggageList; // 예약한 짐 목록

    private LocalDateTime startDateTime; // 예약 시작 일자
    private LocalDateTime endDateTime; // 예약 만료 일자
    private Integer paymentAmount; // 결제 금액

    @Enumerated(EnumType.STRING)
    private StorageReservationStatus status; // 예약 상태 (승인, 거절, 대기 중)
}
