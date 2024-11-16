package com.jkm.jimkanman.client.geocoding;

import com.jkm.jimkanman.domain.Coordinate;
import org.springframework.stereotype.Component;

@Component
public interface GeocodingAdapter {
    Coordinate getCoordinates(String address);
}
