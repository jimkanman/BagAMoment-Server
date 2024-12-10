package com.jkm.jimkanman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

public class DeliveryRequest {
    private DeliveryRequest(){}

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @Schema(name = "DeliveryRequest.ReservationDto")
    public static class ReservationDto {
        private List<ReservationResponse.LuggageDto> luggage;

        @NotBlank
        private String destinationPostalCode;

        @NotBlank
        private String destinationAddress;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String startDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String endDateTime;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜는 yyyy-MM-dd'T'HH:mm:ss형식이여야 합니다.")
        private String deliveryArrivalDateTime;
    }
}
