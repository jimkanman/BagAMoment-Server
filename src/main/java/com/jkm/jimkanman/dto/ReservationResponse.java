package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.converter.StringToDateTimeConverter;
import com.jkm.jimkanman.domain.StorageReservation;
import com.jkm.jimkanman.global.JimkanmanConstants;
import lombok.*;

import java.time.format.DateTimeFormatter;
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
        private List<ReservationRequest.LuggageDto> luggage;
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
                        .map(luggage -> new ReservationRequest.LuggageDto(luggage))
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

        private Long storageId;
        private String storageName;
        private String previewImagePath;
        private List<ReservationRequest.LuggageDto> luggage;
        private DeliveryResponse.ReservationDto deliveryReservation;

        private String startDateTime;
        private String endDateTime;

        private Integer paymentAmount;
        private String status;

        public ReservationPreviewDto(StorageReservation reservation) {
            if (reservation == null) return;
            id = reservation.getId();
            previewImagePath = JimkanmanConstants.DEFAULT_PREVIEW_IMAGE_PATH;
            storageId = reservation.getId();
            storageName = reservation.getStorage().getName();

            if(reservation.getDeliveryReservation() != null) {
                deliveryReservation = new DeliveryResponse.ReservationDto(reservation.getDeliveryReservation());
            }

            if(reservation.getLuggageList() != null) {
                luggage = reservation.getLuggageList().stream()
                        .map(luggage -> new ReservationRequest.LuggageDto(luggage))
                        .toList();
            }

            startDateTime = StringToDateTimeConverter.toDateString(reservation.getStartDateTime());
            endDateTime = StringToDateTimeConverter.toDateString(reservation.getEndDateTime());
            paymentAmount = reservation.getPaymentAmount();
            status = reservation.getStatus().name().toLowerCase();
        }

        public ReservationPreviewDto(StorageReservation reservation, String imagePath) {
            if (reservation == null) return;
            id = reservation.getId();
            previewImagePath = imagePath;
            storageId = reservation.getId();
            storageName = reservation.getStorage().getName();

            if(reservation.getDeliveryReservation() != null) {
                deliveryReservation = new DeliveryResponse.ReservationDto(reservation.getDeliveryReservation());
            }

            if(reservation.getLuggageList() != null) {
                luggage = reservation.getLuggageList().stream()
                        .map(luggage -> new ReservationRequest.LuggageDto(luggage))
                        .toList();
            }

            startDateTime = StringToDateTimeConverter.toDateString(reservation.getStartDateTime());
            endDateTime = StringToDateTimeConverter.toDateString(reservation.getEndDateTime());
            paymentAmount = reservation.getPaymentAmount();
            status = reservation.getStatus().name().toLowerCase();
        }
    }
}
