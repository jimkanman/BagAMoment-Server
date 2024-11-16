package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Storage;

import java.util.List;
import java.util.stream.Collectors;

public class StorageResponse {
    public static class SimpleStorageDto {
        private Long id;
        private String name;
        public SimpleStorageDto(Storage storage){
            if(storage == null) return;
            id = storage.getId();
            name = storage.getName();
        }
    }

    public static class StorageDto {
        private Long id;

        private String name;
        private Long ownerId;
        private String phoneNumber;
        private String description;
        private String notice;

        // 주소 관련
        private String postalCode; // 우편번호
        private String detailedAddress; // 상세주소
        private Double latitude; // 위도
        private Double longitude; // 경도

        // 운영 시간
        private String openingTime; // 시작시간
        private String closingTime; // 종료시간

        private int backpackPricePerHour;
        private int carrierPricePerHour;
        private int miscellaneousItemPricePerHour;

        // 약관 파일명
        private String termsAndConditions;

        private List<String> images;
        private List<String> storageOptions;
        public StorageDto(Storage storage){
            this.id = storage.getId();
            this.name = storage.getName();
            this.ownerId = storage.getOwner().getId();
            this.phoneNumber = storage.getPhoneNumber();
            this.description = storage.getDescription();
            this.notice = storage.getNotice();

            // 주소 관련
            this.postalCode = storage.getPostalCode();
            this.detailedAddress = storage.getDetailedAddress();
            this.latitude = storage.getLatitude();
            this.longitude = storage.getLongitude();

            // 운영 시간
            this.openingTime = storage.getOpeningTime();
            this.closingTime = storage.getClosingTime();

            // 가격 정책
            this.backpackPricePerHour = storage.getBackpackPricePerHour();
            this.carrierPricePerHour = storage.getCarrierPricePerHour();
            this.miscellaneousItemPricePerHour = storage.getMiscellaneousItemPricePerHour();

            // 약관 파일명
            this.termsAndConditions = storage.getTermsAndConditions();

            // 이미지 파일명 리스트 생성
            this.images = storage.getStorageImages().stream()
                    .map(storageImage -> storageImage.getOriginalFileName())
                    .collect(Collectors.toList());

            // 저장 옵션 Enum을 문자열로 변환하여 리스트 생성
            this.storageOptions = storage.getStorageOption().stream()
                    .map(storageOption -> storageOption.name())
                    .collect(Collectors.toList());
        }
    }
}
