package com.jkm.jimkanman.dto;

import com.jkm.jimkanman.domain.Delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long deliveryId;
    private Double latitude;
    private Double longitude;

    public LocationDto(Delivery delivery) {
        deliveryId = delivery.getId();
        latitude = delivery.getLatitude();
        longitude = delivery.getLongitude();
    }
}
