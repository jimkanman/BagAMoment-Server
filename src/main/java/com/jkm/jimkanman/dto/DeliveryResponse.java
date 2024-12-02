package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.converter.StringToDateTimeConverter;
import com.jkm.jimkanman.domain.Delivery;
import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import lombok.*;

import java.util.List;

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
        private List<ReservationRequest.LuggageDto> luggage;

        private String storageAddress;
        private String storagePostalCode;
        private String destinationAddress;
        private String destinationPostalCode;
        private Double destinationLatitude;
        private Double destinationLongitude;

        private DeliveryStatus status;

        public ReservationDto(DeliveryReservation deliveryReservation) {
            id = deliveryReservation.getId();
            deliveryId = deliveryReservation.getDelivery().getId();
            deliveryArrivalDateTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDeliveryArrivalDateTime());

            if(deliveryReservation.getStorageReservation().getLuggageList() != null) {
                luggage = deliveryReservation.getStorageReservation().getLuggageList().stream()
                        .map(luggageEntity -> new ReservationRequest.LuggageDto(luggageEntity))
                        .toList();
            }

            storageAddress = deliveryReservation.getStorageReservation().getStorage().getDetailedAddress();
            storagePostalCode = deliveryReservation.getStorageReservation().getStorage().getPostalCode();
            destinationAddress = deliveryReservation.getDestinationAddress();
            destinationPostalCode = deliveryReservation.getDestinationPostalCode();
            destinationLatitude = deliveryReservation.getDestinationLatitude();
            destinationLongitude = deliveryReservation.getDestinationLongitude();

            status = deliveryReservation.getStatus();
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
            status = delivery.getDeliveryReservation().getStatus().name();
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
        private String address;
        private String arrivalTime;
        private String status;

        public DeliveryDto(Delivery delivery) {
            id = delivery.getId();
            latitude = delivery.getLatitude();
            longitude = delivery.getLongitude();
            // TODO ADDESS 넣기 (Delivery에? DeliveryReservation에?)
            arrivalTime = StringToDateTimeConverter.toDateString(delivery.getArrivalTime());
            status = delivery.getDeliveryReservation().getStatus().name();
        }
    }

    @Data
    public static class DeliveryExtendedDto {

    }
}
