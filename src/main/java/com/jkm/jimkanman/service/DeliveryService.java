package com.jkm.jimkanman.service;

import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.DeliveryResponse;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    DeliveryResponse.ReservationDto makeDeliveryReservation(Long reservationId, DeliveryRequest.ReservationDto reservationDto);


    Object assignDelivery(Long deliveryId);

    Object getPendingDeliveries();

    Object startDelivery(Long deliveryId, Double latitude, Double longitude);
}
