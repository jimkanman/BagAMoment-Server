package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.enums.LuggageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReservationRequest {
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "ReservationRequest.ReservationWithLuggageImageDto")
    public static class ReservationWithLuggageImageDto {
        private List<MultipartLuggageDto> luggage;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String startDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String endDateTime;

//        public ReservationWithLuggageImageDto(DeliveryRequest.ReservationWithLuggageImageDto reservation) {
//            luggage = reservation.getLuggage();
//            startDateTime = reservation.getStartDateTime();
//            endDateTime = reservation.getEndDateTime();
//        }
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "ReservationRequest.ReservationDto")
    static public class ReservationDto {
        private List<ReservationRequest.PlainLuggageDto> luggage;

        // 날짜: ISO-8601 형식 (ex. "2023-11-15T14:30:00")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String startDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String endDateTime;

        public ReservationDto(DeliveryRequest.ReservationDto reservationDto) {
            luggage = reservationDto.getLuggage();
            startDateTime = reservationDto.getStartDateTime();
            endDateTime = reservationDto.getEndDateTime();
            if(endDateTime == null) endDateTime = reservationDto.getDeliveryArrivalDateTime();
        }

//        public ReservationDto(DeliveryRequest.ReservationWithLuggageImageDto reservationDto) {
//            luggage = reservationDto.getLuggage().stream()
//                    .map(l -> new ReservationResponse.LuggageDto(l))
//                    .toList();
//            startDateTime = reservationDto.getStartDateTime();
//            endDateTime = reservationDto.getEndDateTime();
//        }
    }


    /** 짐 정보 Dto (요청용: 이미지 파일 포함 (MultipartFile)) */
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "MultipartLuggageDto (ReservationRequest)")
    static public class MultipartLuggageDto {
        private LuggageType type;
        private MultipartFile imageFile;
        private Integer width;
        private Integer depth;
        private Integer height;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "PlainLuggageDto (No Image)")
    public static class PlainLuggageDto {
        private LuggageType type;
        private Integer width;
        private Integer depth;
        private Integer height;
    }
}
