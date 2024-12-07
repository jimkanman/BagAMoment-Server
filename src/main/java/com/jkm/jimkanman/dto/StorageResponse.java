package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Storage;
import com.jkm.jimkanman.domain.enums.StorageRegistrationStatus;
import com.jkm.jimkanman.global.JimkanmanConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StorageResponse {
    @Getter
    @NoArgsConstructor
    public static class SimpleStorageDto {
        private Long id;
        private String name;
        public SimpleStorageDto(Storage storage){
            if(storage == null) return;
            id = storage.getId();
            name = storage.getName();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class StoragePreviewDto {
        private long id;
        private String previewImagePath;
        private String name;
        private List<String> storageOptions;
        private Boolean hasDeliveryService;

        private String postalCode;
        private String detailedAddress;
        private Double latitude;
        private Double longitude;
        private Double distance;

        private String openingTime;
        private String closingTime;
        private Boolean isOpen;

        public StoragePreviewDto(Storage storage, Double distanceFromCurrentLocation) {
            id = storage.getId();
            name = storage.getName();
            distance = distanceFromCurrentLocation;
            hasDeliveryService = storage.getHasDeliveryService();
            openingTime = storage.getOpeningTime();
            closingTime = storage.getClosingTime();
            this.isOpen = determineIsOpen(openingTime, closingTime);;
            latitude = storage.getLatitude();
            longitude = storage.getLongitude();
            detailedAddress = storage.getDetailedAddress();
            postalCode = storage.getPostalCode();

            if(storage.getStorageImages() != null) {
                previewImagePath = storage.getStorageImages().stream()
                        .findFirst()
                        .map(image -> image.getStoredFileName())
                        .orElse(JimkanmanConstants.DEFAULT_PREVIEW_IMAGE_PATH); // 이미지 빈 경우 디폴트 썸네일
            }

            if(storage.getStorageOption() != null) {
                storageOptions = storage.getStorageOption().stream()
                        .map(storageOption -> storageOption.name())
                        .toList();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class StorageDto {
        private Long id;
        private String name;
        private Long ownerId;
        private String phoneNumber;
        private String description;
        private String notice;
        private Boolean hasDeliveryService;

        // 주소 관련
        private String postalCode; // 우편번호
        private String detailedAddress; // 상세주소
        private Double latitude; // 위도
        private Double longitude; // 경도

        // 운영 시간
        private String openingTime; // 시작시간
        private String closingTime; // 종료시간
        private Boolean isOpen;

        private int backpackPricePerHour;
        private int carrierPricePerHour;
        private int miscellaneousItemPricePerHour;

        // 약관 파일명
        private String termsAndConditions;

        // 기타
        private List<String> images;
        private List<String> storageOptions;
        private StorageRegistrationStatus status;

        public StorageDto(Storage storage){
            this.id = storage.getId();
            this.name = storage.getName();
            this.ownerId = storage.getOwner().getId();
            this.phoneNumber = storage.getPhoneNumber();
            this.description = storage.getDescription();
            this.notice = storage.getNotice();
            this.hasDeliveryService = storage.getHasDeliveryService();

            // 주소 관련
            this.postalCode = storage.getPostalCode();
            this.detailedAddress = storage.getDetailedAddress();
            this.latitude = storage.getLatitude();
            this.longitude = storage.getLongitude();

            // 운영 시간
            this.openingTime = storage.getOpeningTime();
            this.closingTime = storage.getClosingTime();
            this.isOpen = determineIsOpen(openingTime, closingTime);

            // 가격 정책
            this.backpackPricePerHour = storage.getBackpackPricePerHour();
            this.carrierPricePerHour = storage.getCarrierPricePerHour();
            this.miscellaneousItemPricePerHour = storage.getMiscellaneousItemPricePerHour();

            // 약관 파일명
            this.termsAndConditions = storage.getTermsAndConditions();

            // 이미지 파일명 리스트 생성
            if(storage.getStorageImages() != null) {
                this.images = storage.getStorageImages().stream()
                        .map(storageImage -> storageImage.getStoredFileName())
                        .collect(Collectors.toList());
            }
            if(this.images == null || this.images.isEmpty()) this.images = List.of(JimkanmanConstants.DEFAULT_PREVIEW_IMAGE_PATH);

            // 저장 옵션 Enum을 문자열로 변환하여 리스트 생성
            if(storage.getStorageOption() != null) {
                this.storageOptions = storage.getStorageOption().stream()
                        .map(storageOption -> storageOption.name())
                        .collect(Collectors.toList());
            }

            if(storage.getStorageRegistration() != null) {
                status = storage.getStorageRegistration().getStatus();
            }
        }
    }

    /** StorageDto + 현재 위치와의 거리 */
    @Getter
    @NoArgsConstructor
    public static class DetailedStorageDto {
        private Long id;
        private String name;
        private Long ownerId;
        private String phoneNumber;
        private String description;
        private String notice;
        private Boolean hasDeliveryService;

        // 주소 관련
        private String postalCode;
        private String detailedAddress;
        private Double latitude;
        private Double longitude;
        private Double distance;

        // 운영 시간
        private String openingTime;
        private String closingTime;
        private Boolean isOpen;

        private int backpackPricePerHour;
        private int carrierPricePerHour;
        private int miscellaneousItemPricePerHour;

        // 약관 파일명
        private String termsAndConditions;

        private List<String> images;
        private List<String> storageOptions;
        public DetailedStorageDto(Storage storage, Double distance){
            this.id = storage.getId();
            this.name = storage.getName();
            this.ownerId = storage.getOwner().getId();
            this.phoneNumber = storage.getPhoneNumber();
            this.description = storage.getDescription();
            this.notice = storage.getNotice();
            this.hasDeliveryService = storage.getHasDeliveryService();

            // 주소 관련
            this.postalCode = storage.getPostalCode();
            this.detailedAddress = storage.getDetailedAddress();
            this.latitude = storage.getLatitude();
            this.longitude = storage.getLongitude();
            this.distance = distance;

            // 운영 시간
            this.openingTime = storage.getOpeningTime();
            this.closingTime = storage.getClosingTime();
            this.isOpen = determineIsOpen(openingTime, closingTime);

            // 가격 정책
            this.backpackPricePerHour = storage.getBackpackPricePerHour();
            this.carrierPricePerHour = storage.getCarrierPricePerHour();
            this.miscellaneousItemPricePerHour = storage.getMiscellaneousItemPricePerHour();

            // 약관 파일명
            this.termsAndConditions = storage.getTermsAndConditions();

            // 이미지 파일명 리스트 생성
            if(storage.getStorageImages() != null) {
                this.images = storage.getStorageImages().stream()
                        .map(storageImage -> storageImage.getStoredFileName())
                        .collect(Collectors.toList());
                if (this.images.isEmpty()) this.images = List.of(JimkanmanConstants.DEFAULT_PREVIEW_IMAGE_PATH);
            }

            // 저장 옵션 Enum을 문자열로 변환하여 리스트 생성
            if(storage.getStorageOption() != null) {
            this.storageOptions = storage.getStorageOption().stream()
                    .map(storageOption -> storageOption.name())
                    .collect(Collectors.toList());
            }
        }
    }

    static Boolean determineIsOpen(String openingTime, String closingTime) {
        try {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return !now.isBefore(LocalTime.parse(openingTime, formatter))
                    && !now.isAfter(LocalTime.parse(closingTime, formatter));
        } catch (Exception e) {
            System.out.println("StorageResponse: Exception while parsing " + openingTime + ", " + closingTime + ": " + e.getMessage());
            return null;
        }
    }
}
