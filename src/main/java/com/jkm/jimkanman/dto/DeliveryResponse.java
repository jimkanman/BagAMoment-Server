package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.converter.StringToDateTimeConverter;
import com.jkm.jimkanman.domain.Delivery;
import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeliveryResponse {
    private DeliveryResponse() {}

    /** 배송 예약에 대한 Dto */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationDto {
        private Long id;
        private Long deliveryId;
        private String deliveryArrivalDateTime;
        private String destinationAddress;
        private String destinationPostalCode;
        private DeliveryStatus status;

        public ReservationDto(DeliveryReservation deliveryReservation) {
            id = deliveryReservation.getId();
            deliveryId = deliveryReservation.getDelivery().getId();
            deliveryArrivalDateTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDeliveryArrivalDateTime());
            destinationAddress = deliveryReservation.getDestinationAddress();
            destinationPostalCode = deliveryReservation.getDestinationPostalCode();
            status = deliveryReservation.getDelivery().getStatus();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleDeliveryDto {
        private Long deliveryId;
        private Long deliveryReservationId;
        private Long storageReservationId;

        private String arrivalTime;
        private String status;

        public SimpleDeliveryDto(Delivery delivery) {
            deliveryId = delivery.getId();
            deliveryReservationId = delivery.getDeliveryReservation().getId();
            storageReservationId = delivery.getDeliveryReservation().getStorageReservation().getId();

            arrivalTime = StringToDateTimeConverter.toDateString(delivery.getArrivalTime());
            status = delivery.getStatus().name();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeliveryDto {
        private Long id;
        private Double latitude;
        private Double longitude;
        private String arrivalTime;
        private String status;

        public DeliveryDto(Delivery delivery) {
            id = delivery.getId();
            latitude = delivery.getLatitude();
            longitude = delivery.getLongitude();
            arrivalTime = StringToDateTimeConverter.toDateString(delivery.getArrivalTime());
            status = delivery.getStatus().name();
        }
    }
}
