package com.jkm.jimkanman.client.geocoding;

import com.jkm.jimkanman.domain.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

@SpringBootTest
class GoogleGeocodingClientTest {
    @Autowired
    GoogleGeocodingClient client;

    @Test
    void 주소_위도경도_검증() {
        List<Coordinate> coordinates = new ArrayList<>();
        String[] addresses = new String[]{"흑석로 84", "동작구 흑석로 84", "동작구 흑석로84", "서울시 동작구 흑석로 84", "서울특별시 동작구 흑석로 84"};

        for(String address: addresses){
            Long startTime = System.currentTimeMillis();
            coordinates.add(client.getCoordinates(address));
            Long endTime = System.currentTimeMillis();
            System.out.println("api took " + (endTime-startTime)/1000f + "s");
        }

        for(Coordinate coordinate: coordinates) {
            Assertions.assertThat(coordinate).isNotNull();
            Assertions.assertThat(coordinate.getLatitude()).isBetween(37.5, 37.51);
            Assertions.assertThat(coordinate.getLongitude()).isBetween(126.95, 126.96);
        }
    }
}