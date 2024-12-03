package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Luggage;
import com.jkm.jimkanman.domain.enums.LuggageType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReservationRequest {
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    static public class ReservationDto {
        private List<LuggageDto> luggage;

        // 날짜: ISO-8601 형식 (ex. "2023-11-15T14:30:00")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String startDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String endDateTime;

        public ReservationDto(DeliveryRequest.ReservationDto reservationDto) {
            luggage = reservationDto.getLuggage();
            startDateTime = reservationDto.getStartDateTime();
            endDateTime = reservationDto.getEndDateTime();
        }
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    static public class LuggageDto {
        private LuggageType type;
        private Integer width;
        private Integer depth;
        private Integer height;

        public LuggageDto(Luggage luggage) {
            type = luggage.getType();
            width = luggage.getWidth();
            depth = luggage.getDepth();
            height = luggage.getHeight();
        }
    }
}
