package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Coordinate;
import org.springframework.stereotype.Component;

@Component
public interface GpsUtil {
    Coordinate convertToCoordinates(String address);
    boolean isWithinRadius(double centerLat, double centerLng, double targetLat, double targetLng, double radiusM);
    double calculateDistance(double latA, double lngA, double latB, double lngB);
    double[] calculateLatLngRangeAroundTarget(double targetLat, double targetLng, int radiusM);
}
