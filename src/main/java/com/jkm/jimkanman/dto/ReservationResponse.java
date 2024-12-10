package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.converter.StringToDateTimeConverter;
import com.jkm.jimkanman.domain.Luggage;
import com.jkm.jimkanman.domain.StorageReservation;
import com.jkm.jimkanman.domain.enums.LuggageType;
import com.jkm.jimkanman.global.JimkanmanConstants;
import lombok.*;

import java.util.List;

public class ReservationResponse {
    @Getter
    @NoArgsConstructor
    public static class ReservationResultDto {
        private Long id;
        private Long storageId;
        private String status;

        public ReservationResultDto(StorageReservation reservation) {
            id = reservation.getId();
            storageId = reservation.getStorage().getId();
            status = reservation.getStatus().name().toLowerCase();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class ReservationDto {
        private Long id;

        private Long storageId;
        private String storageName;
        private List<LuggageDto> luggage;
        private DeliveryResponse.ReservationDto deliveryReservation;

        private String startDateTime;
        private String endDateTime;

        private Integer paymentAmount;
        private String status;

        public ReservationDto(StorageReservation reservation) {
            if (reservation == null) return;
            id = reservation.getId();

            storageId = reservation.getId();
            storageName = reservation.getStorage().getName();

            if(reservation.getDeliveryReservation() != null) {
                deliveryReservation = new DeliveryResponse.ReservationDto(reservation.getDeliveryReservation());
            }

            if(reservation.getLuggageList() != null) {
                luggage = reservation.getLuggageList().stream()
                        .map(luggage -> new LuggageDto(luggage))
                        .toList();
            }

            startDateTime = StringToDateTimeConverter.toDateString(reservation.getStartDateTime());
            endDateTime = StringToDateTimeConverter.toDateString(reservation.getEndDateTime());
            paymentAmount = reservation.getPaymentAmount();
            status = reservation.getStatus().name().toLowerCase();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class ReservationPreviewDto {
        private Long id;
        private Long memberId;
        private String memberNickname;

        private Long storageId;
        private String storageName;
        private String previewImagePath;
        private String storageAddress;
        private List<LuggageDto> luggage;
        private DeliveryResponse.ReservationDto deliveryReservation;

        private String startDateTime;
        private String endDateTime;

        private Integer paymentAmount;
        private String status;

        public ReservationPreviewDto(StorageReservation reservation) {
            previewImagePath = JimkanmanConstants.DEFAULT_PREVIEW_IMAGE_PATH;
            fillDtoFields(reservation);
        }

        public ReservationPreviewDto(StorageReservation reservation, String imagePath) {
            previewImagePath = imagePath;
            fillDtoFields(reservation);
        }

        private void fillDtoFields(StorageReservation reservation) {
            if (reservation == null) return;
            id = reservation.getId();
            if(reservation.getMember() != null) {
                memberId = reservation.getMember().getId();
                memberNickname = reservation.getMember().getNickname();
            }
            storageId = reservation.getId();
            storageName = reservation.getStorage().getName();
            storageAddress = reservation.getStorage().getDetailedAddress();

            if(reservation.getDeliveryReservation() != null) {
                deliveryReservation = new DeliveryResponse.ReservationDto(reservation.getDeliveryReservation());
            }

            if(reservation.getLuggageList() != null) {
                luggage = reservation.getLuggageList().stream()
                        .map(luggage -> new LuggageDto(luggage))
                        .toList();
            }

            startDateTime = StringToDateTimeConverter.toDateString(reservation.getStartDateTime());
            endDateTime = StringToDateTimeConverter.toDateString(reservation.getEndDateTime());
            paymentAmount = reservation.getPaymentAmount();
            status = reservation.getStatus().name().toLowerCase();
        }
    }

    /** 짐 정보 Dto (응답용: 이미지 경로 포함 (String)) */
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    static public class LuggageDto {
        private LuggageType type;
        private String imagePath;
        private Integer width;
        private Integer depth;
        private Integer height;

        public LuggageDto(Luggage luggage) {
            type = luggage.getType();
            imagePath = luggage.getImagePath();
            width = luggage.getWidth();
            depth = luggage.getDepth();
            height = luggage.getHeight();
        }
    }
}
