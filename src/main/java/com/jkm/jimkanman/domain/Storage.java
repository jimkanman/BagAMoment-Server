package com.jkm.jimkanman.domain;

import com.jkm.jimkanman.converter.StorageOptionConverter;
import com.jkm.jimkanman.domain.enums.StorageOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storage")
@Builder
public class Storage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 보관소명
    private String phoneNumber; // 보관소 전화번호
    private String description; // 보관소 소개
    private String notice; // 공지사항

    // 주소 관련
    private String postalCode; // 우편번호
    private String detailedAddress; // 상세주소
    private Double latitude; // 위도
    private Double longitude; // 경도

    // 운영 시간
    private String openingTime; // 시작시간
    private String closingTime; // 종료시간

    // TODO 가격 정책 저장 방식
    private int backpackPricePerHour;
    private int carrierPricePerHour;
    private int otherPricePerHour;

    // 약관 파일명
    private String termsAndConditions;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageImage> storageImages = new ArrayList<>();

    // 옵션 (List<StorageOption>을 converter로 String 변환하여 저장)
    @Convert(converter = StorageOptionConverter.class)
    private List<StorageOption> storageOption;
}
