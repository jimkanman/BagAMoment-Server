package com.jkm.jimkanman.client.geocoding;

import com.jkm.jimkanman.client.geocoding.VWorldClient;
import com.jkm.jimkanman.domain.Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/** vWorldClient 대신 GoogleGeocodingClient 쓰므로 테스트 필요 X */
/*
@SpringBootTest
class VWorldClientTest {
    @Autowired
    private VWorldClient vWorldClient;

    @Test
    void getCoordinates() {
        String address = "%ED%9A%A8%EB%A0%B9%EB%A1%9C72%EA%B8%B8%2060";
        System.out.println("VWorldClientTest: sending api...");
        Long startTime = System.currentTimeMillis();
        Coordinate coordinate = vWorldClient.getCoordinates(address);
        Long endTime = System.currentTimeMillis();
        System.out.println("VWorldClientTest: api took " + (endTime-startTime) / 1000f + "s");

        assertNotNull(coordinate, "Coordinate should not be null");
        System.out.println("Latitude: " + coordinate.getLatitude());
        System.out.println("Longitude: " + coordinate.getLongitude());
    }
}
 */