package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Coordinate;
import org.springframework.stereotype.Component;

@Component
public interface GpsUtil {
    Coordinate convertToCoordinates(String address);
    boolean isWithinRadius(double centerLat, double centerLng, double targetLat, double targetLng, double radiusKm);
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    double[] calculateLatLngRangeAroundTarget(double targetLat, double targetLng, int radiusKm);
}
