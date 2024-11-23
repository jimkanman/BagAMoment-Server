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
     * 두 GPS 좌표 간의 거리를 계산하여 반환 (단위: m)
     */
    @Override
    public double calculateDistance(double latA, double lngA, double latB, double lngB) {
        double latDistance = Math.toRadians(latB - latA);
        double lonDistance = Math.toRadians(lngB - lngA);

        // Haversine 공식으로 거리 계산
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latA)) * Math.cos(Math.toRadians(latB))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c * 1000; // 1000을 곱해서 미터로 변환
    }

    @Override
    public double[] calculateLatLngRangeAroundTarget(double targetLat, double targetLng, int radiusM) {
        // 위도 경도는 1도당 약 111.32km
        double radiusKm = radiusM / 1000.0;
        double latDegreeRange = radiusKm / 111.32;
        double lngDegreeRange = radiusKm / (111.32 * Math.cos(Math.toRadians(targetLat))); // 경도의 범위 계산

        double minLat = targetLat - latDegreeRange;
        double maxLat = targetLat + latDegreeRange;
        double minLng = targetLng - lngDegreeRange;
        double maxLng = targetLng + lngDegreeRange;

        System.out.printf("%dkm range from (%f, %f): lat=(%f~%f), lng=(%f~%f)\n", radiusM, targetLat, targetLng, minLat, maxLat, minLng, maxLng);
        return new double[]{minLat, maxLat, minLng, maxLng};
    }

    /**
     * target 좌표가 기준 좌표에서 지정한 반경 내에 있는지 여부를 반환함.
     */
    @Override
    public boolean isWithinRadius(double centerLat, double centerLng, double targetLat, double targetLng, double radiusM) {
        double distance = calculateDistance(centerLat, centerLng, targetLat, targetLng);
        return distance <= radiusM;
    }
}
