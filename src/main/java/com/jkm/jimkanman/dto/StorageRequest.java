package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.enums.StorageOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StorageRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StorageRegisterDto {
        private String registerName;
        private String phoneNumber;
        private String description;
        private String postalCode;
        private String detailedAddress;
        private String openingTime;
        private String closingTime;
        private int backpackPricePerHour;
        private int carrierPricePerHour;
        private int miscellaneousItemPricePerHour;
        private MultipartFile termsAndConditions;
        private List<MultipartFile> storageImages;
        private List<StorageOption> storageOptions;
    }
}
