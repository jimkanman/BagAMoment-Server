package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.StorageReservation;
import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationResponse {
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class ReservationDto {
        private Long id;

        private Long storageId;
        private String storageName;
        // TODO 보관소 썸네일
        private List<ReservationRequest.LuggageDto> luggage;
        private DeliveryReservationDto delivery;

        private String startDateTime;
        private String endDateTime;

        private Integer paymentAmount;
        private String status;

        public ReservationDto(StorageReservation reservation) {
            if (reservation == null) return;
            id = reservation.getId();

            storageId = reservation.getId();
            storageName = reservation.getStorage().getName();
            luggage = reservation.getLuggageList().stream()
                    .map(luggage -> new ReservationRequest.LuggageDto(luggage))
                    .toList();
            delivery = new DeliveryReservationDto(reservation.getDeliveryReservation());

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            startDateTime = reservation.getStartDateTime().format(formatter);
            endDateTime = reservation.getEndDateTime().format(formatter);

            paymentAmount = reservation.getPaymentAmount();
            status = reservation.getStatus().name().toLowerCase();
        }
    }

    public static class DeliveryReservationDto {
        public DeliveryReservationDto(DeliveryReservation deliveryReservation) {
            if (deliveryReservation == null) return;
        }
    }
}
