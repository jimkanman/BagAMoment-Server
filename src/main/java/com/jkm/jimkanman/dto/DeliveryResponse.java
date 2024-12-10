package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.converter.StringToDateTimeConverter;
import com.jkm.jimkanman.domain.Delivery;
import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class DeliveryResponse {
    private DeliveryResponse() {}

    /** 배송 예약에 대한 Dto */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "DeliveryResponse.ReservationDto")
    public static class ReservationDto {
        private Long id;
        private Long deliveryId;
        private Long storageId;

        private String deliveryArrivalDateTime;
        private List<ReservationResponse.LuggageDto> luggage;

        // 위치 관련 정보
        private String storageAddress;
        private String storagePostalCode;
        private Double storageLatitude;
        private Double storageLongitude;

        private String destinationAddress;
        private String destinationPostalCode;
        private Double destinationLatitude;
        private Double destinationLongitude;

        private Double distance;
        private DeliveryStatus status;

        public ReservationDto(DeliveryReservation deliveryReservation) {
            id = deliveryReservation.getId();
            deliveryId = deliveryReservation.getDelivery().getId();
            storageId = deliveryReservation.getStorageReservation().getStorage().getId();

            deliveryArrivalDateTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDeliveryArrivalDateTime());

            if(deliveryReservation.getStorageReservation().getLuggageList() != null) {
                luggage = deliveryReservation.getStorageReservation().getLuggageList().stream()
                        .map(luggageEntity -> new ReservationResponse.LuggageDto(luggageEntity))
                        .toList();
            }

            storageAddress = deliveryReservation.getStorageReservation().getStorage().getDetailedAddress();
            storagePostalCode = deliveryReservation.getStorageReservation().getStorage().getPostalCode();
            storageLatitude = deliveryReservation.getStorageReservation().getStorage().getLatitude();
            storageLongitude = deliveryReservation.getStorageReservation().getStorage().getLongitude();

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
    public static class DeliveryAndReservationDto {
        private Long deliveryReservationId;
        private Long deliveryId;
        private Long storageId;

        private String deliveryArrivalDateTime;
        private List<ReservationResponse.LuggageDto> luggage;

        private String storageAddress;
        private String storagePostalCode;
        private Double storageLatitude;
        private Double storageLongitude;

        private String destinationAddress;
        private String destinationPostalCode;
        private Double destinationLatitude;
        private Double destinationLongitude;

        private Double distance;
        private DeliveryStatus status;

        private Double latitude;
        private Double longitude;
        private String arrivalTime;

        public DeliveryAndReservationDto(DeliveryReservation deliveryReservation) {
            deliveryReservationId = deliveryReservation.getId();
            deliveryId = deliveryReservation.getDelivery().getId();
            storageId = deliveryReservation.getStorageReservation().getStorage().getId();

            deliveryArrivalDateTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDeliveryArrivalDateTime());

            if(deliveryReservation.getStorageReservation().getLuggageList() != null) {
                luggage = deliveryReservation.getStorageReservation().getLuggageList().stream()
                        .map(luggageEntity -> new ReservationResponse.LuggageDto(luggageEntity))
                        .toList();
            }

            storageAddress = deliveryReservation.getStorageReservation().getStorage().getDetailedAddress();
            storagePostalCode = deliveryReservation.getStorageReservation().getStorage().getPostalCode();
            storageLatitude = deliveryReservation.getStorageReservation().getStorage().getLatitude();
            storageLongitude = deliveryReservation.getStorageReservation().getStorage().getLongitude();

            destinationAddress = deliveryReservation.getDestinationAddress();
            destinationPostalCode = deliveryReservation.getDestinationPostalCode();
            destinationLatitude = deliveryReservation.getDestinationLatitude();
            destinationLongitude = deliveryReservation.getDestinationLongitude();

            status = deliveryReservation.getStatus();

            latitude = deliveryReservation.getDelivery().getLatitude();
            longitude = deliveryReservation.getDelivery().getLongitude();
            arrivalTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDelivery().getArrivalTime());
        }

        public DeliveryAndReservationDto(Delivery delivery) {
            DeliveryReservation deliveryReservation = delivery.getDeliveryReservation();
            deliveryReservationId = deliveryReservation.getId();
            deliveryId = deliveryReservation.getDelivery().getId();
            storageId = deliveryReservation.getStorageReservation().getStorage().getId();

            deliveryArrivalDateTime = StringToDateTimeConverter.toDateString(deliveryReservation.getDeliveryArrivalDateTime());

            if(deliveryReservation.getStorageReservation().getLuggageList() != null) {
                luggage = deliveryReservation.getStorageReservation().getLuggageList().stream()
                        .map(luggageEntity -> new ReservationResponse.LuggageDto(luggageEntity))
                        .toList();
            }

            storageAddress = deliveryReservation.getStorageReservation().getStorage().getDetailedAddress();
            storagePostalCode = deliveryReservation.getStorageReservation().getStorage().getPostalCode();
            storageLatitude = deliveryReservation.getStorageReservation().getStorage().getLatitude();
            storageLongitude = deliveryReservation.getStorageReservation().getStorage().getLongitude();

            destinationAddress = deliveryReservation.getDestinationAddress();
            destinationPostalCode = deliveryReservation.getDestinationPostalCode();
            destinationLatitude = deliveryReservation.getDestinationLatitude();
            destinationLongitude = deliveryReservation.getDestinationLongitude();

            status = deliveryReservation.getStatus();

            latitude = delivery.getLatitude();
            longitude = delivery.getLongitude();
            arrivalTime = StringToDateTimeConverter.toDateString(delivery.getArrivalTime());
        }
    }
}
