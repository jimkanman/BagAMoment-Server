package com.jkm.jimkanman.client.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkm.jimkanman.domain.Coordinate;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

//@Component
public class VWorldClient implements GeocodingAdapter {
    private final String apiKey;
    private final String BASE_URL = "https://api.vworld.kr/req/address";
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public VWorldClient(@Value("${api-keys.vworld}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();

        System.out.println("VWorldClient initialized with api key " + apiKey );
    }

    @Override
    public Coordinate getCoordinates(String address) {
        String uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("service", address)
                .queryParam("request", "getCoord")
                .queryParam("version", "2.0")
                .queryParam("crs", "epsg:4326")
                .queryParam("address", address)
                .queryParam("refine", "true")
                .queryParam("simple", "false")
                .queryParam("format", "json")
                .queryParam("type", "road")
                .queryParam("key", apiKey)
                .build()
                .toUriString();


        VWorldApiResponse apiResponse = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(VWorldApiResponse.class)
                .block();

        if(apiResponse == null)
            throw new BusinessException(ErrorCode.EXTERNAL_API_UNAVAILABLE);

        VWorldResponse response = apiResponse.getResponse();
        if(!"ok".equalsIgnoreCase(response.getStatus())){
            System.out.println("VWorldClient: exception on address '" + address + "'");
            System.out.println("VWorldClient: response = '" + response + "'");
            throw new BusinessException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        // result를 파싱하여 Coordinate으로 반환
        System.out.println("VWorldClient: received response " + response.getResult());
        VWorldResponse.Point point = response.getResult().getPoint();

        return new Coordinate(point.getX(), point.getY());

    }

    @Getter
    @NoArgsConstructor
    @ToString
    private static class VWorldApiResponse {
        private VWorldResponse response;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    private static class VWorldResponse {
        private String status;
        private VWorldResult result;
        private VWorldError error;

        @Getter
        @NoArgsConstructor
        @ToString
        static class VWorldResult {
            private String crs;
            private Point point;
        }

        @Getter
        @NoArgsConstructor
        @ToString
        static class VWorldError {
            private int level;
            private String code;
            private String text;
        }

        @Getter
        @NoArgsConstructor
        @ToString
        static class Point {
            private double x;
            private double y;
        }

    }
}
