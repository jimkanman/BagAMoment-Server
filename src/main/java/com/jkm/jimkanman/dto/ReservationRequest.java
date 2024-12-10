package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.enums.LuggageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReservationRequest {
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "ReservationRequest.ReservationWithLuggageImageDto")
    public static class ReservationWithLuggageImageDto {
        private List<LuggageDto> luggage;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String startDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String endDateTime;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "ReservationRequest.ReservationDto")
    static public class ReservationDto {
        private List<ReservationResponse.LuggageDto> luggage;

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
    }


    /** 짐 정보 Dto (요청용: 이미지 파일 포함 (MultipartFile)) */
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "LuggageDto (ReservationRequest)")
    static public class LuggageDto {
        private LuggageType type;
        private MultipartFile imageFile;
        private Integer width;
        private Integer depth;
        private Integer height;
    }
}
