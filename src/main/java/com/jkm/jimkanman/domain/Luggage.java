package com.jkm.jimkanman.domain;

import com.jkm.jimkanman.domain.enums.LuggageType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "luggage")
@Builder
public class Luggage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = true)
    private String imagePath; // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private StorageReservation reservation;

    @Enumerated(EnumType.STRING)
    private LuggageType type; // 짐 종류 (배낭, 캐리어, 비정형 등)

    private Integer width; // 부피 - 가로
    private Integer depth; // 부피 - 세로
    private Integer height; // 부피 - 높이

    public void setReservation(StorageReservation storageReservation) {
        if(this.reservation != null) this.reservation.getLuggageList().remove(this);
        this.reservation = storageReservation;
        storageReservation.getLuggageList().add(this);
    }
}
