package com.jkm.jimkanman.service;

import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.DeliveryResponse;
import com.jkm.jimkanman.dto.LocationDto;
import com.jkm.jimkanman.dto.ReservationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {
    ReservationResponse.ReservationDto makeDeliveryReservation(Long reservationId, DeliveryRequest.ReservationDto reservationDto);

    DeliveryResponse.SimpleDeliveryDto assignDelivery(Long deliveryId);

    List<DeliveryResponse.ReservationDto> getPendingDeliveries();

    Object startDelivery(Long deliveryId, Double latitude, Double longitude);

    void updateDeliveryLocation(LocationDto request);

    LocationDto getDeliveryLocation(Long deliveryId);

    DeliveryResponse.DeliveryDto findDeliveryById(Long deliveryId);

    DeliveryResponse.ReservationDto findDeliveryReservationById(Long deliveryReservationId);

    void cancelDelivery(Long deliveryId);

    DeliveryResponse.DeliveryAndReservationDto endDelivery(Long deliveryId);
}
