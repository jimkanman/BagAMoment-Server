package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.enums.StorageOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StorageRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StorageRegisterDto {
        @NotBlank
        private String registerName;
        private String phoneNumber;
        private String description;
        private String postalCode;
        @NotBlank
        private String detailedAddress;

        @Pattern(regexp = "^(?:[01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다.")
        private String openingTime;

        @Pattern(regexp = "^(?:[01][0-9]|2[0-3]):[0-5][0-9]$", message = "올바른 시간 형식이 아닙니다.")
        private String closingTime;

        private int backpackPricePerHour;
        private int carrierPricePerHour;
        private int miscellaneousItemPricePerHour;

        private MultipartFile termsAndConditions;
        private List<MultipartFile> storageImages;
        private List<String> storageOptions;
    }
}
