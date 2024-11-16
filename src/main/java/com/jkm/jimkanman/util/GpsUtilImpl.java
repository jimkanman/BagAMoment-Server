package com.jkm.jimkanman.util;

import com.jkm.jimkanman.client.geocoding.GeocodingAdapter;
import com.jkm.jimkanman.domain.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GpsUtilImpl implements GpsUtil {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final GeocodingAdapter geocodingAdapter;

    @Override
    public Coordinate convertToCoordinates(String address) {
        return geocodingAdapter.getCoordinates(address);
    }



    /**
     * 두 GPS 좌표 간의 거리를 계산하여 반환 (단위: km)
     */
    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Haversine 공식으로 거리 계산
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public double[] calculateLatLngRangeAroundTarget(double targetLat, double targetLng, int radiusKm) {
        // 위도 경도는 1도당 약 111.32km
        double latDegreeRange = radiusKm / 111.32;
        double lngDegreeRange = radiusKm / (111.32 * Math.cos(Math.toRadians(targetLat))); // 경도의 범위 계산

        double minLat = targetLat - latDegreeRange;
        double maxLat = targetLat + latDegreeRange;
        double minLng = targetLng - lngDegreeRange;
        double maxLng = targetLng + lngDegreeRange;

        System.out.printf("%dkm range from (%f, %f): lat=(%f~%f), lng=(%f~%f)\n", radiusKm, targetLat, targetLng, minLat, maxLat, minLng, maxLng);
        return new double[]{minLat, maxLat, minLng, maxLng};
    }

    /**
     * target 좌표가 기준 좌표에서 지정한 반경 내에 있는지 여부를 반환함.
     */
    @Override
    public boolean isWithinRadius(double centerLat, double centerLng, double targetLat, double targetLng, double radiusKm) {
        double distance = calculateDistance(centerLat, centerLng, targetLat, targetLng);
        return distance <= radiusKm;
    }
}
