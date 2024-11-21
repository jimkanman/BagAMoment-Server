package com.jkm.jimkanman.client.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkm.jimkanman.domain.Coordinate;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class GoogleGeocodingClient implements GeocodingAdapter {
    private final String apiKey;
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();
    private final ObjectMapper objectMapper;

    public GoogleGeocodingClient(@Value("${api-keys.google}")final String apiKey, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }
    @Override
    public Coordinate getCoordinates(String address) {
        if(address == null) throw new BusinessException(ErrorCode.STORAGE_ADDRESS_NULL);

        String uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("address", address.replace(" ", "+"))
                .queryParam("key", apiKey)
                .queryParam("language", "ko")
                .build()
                .toUriString();
        System.out.println("GoogleClient: sending GET request to " + uri);

        GoogleApiResponse apiResponse = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GoogleApiResponse.class)
                .block();

        System.out.println("GoogleClient: received response;");
        System.out.println(apiResponse);
        if (apiResponse == null || !"ok".equalsIgnoreCase(apiResponse.getStatus())) {
            if (apiResponse != null) {
                System.out.println("GoogleClient: exception with address " + address + "; Api status is " + apiResponse.getStatus());
                System.out.println("GoogleClient: error message = " + apiResponse.getResults());
            }
            throw new RuntimeException(apiResponse != null ? "API 호출 실패" : "API 응답이 null 입니다");
        }

        GoogleApiResponse.GoogleGeometry geometry = apiResponse.getResults().get(0).getGeometry();

        return new Coordinate(geometry.getLocation().getLat(), geometry.getLocation().getLng());
    }

    @Getter
    @ToString
    @NoArgsConstructor
    static class GoogleApiResponse {
        private String status;
        private List<GoogleResult> results;

        @Getter
        @ToString
        @NoArgsConstructor
        static class GoogleResult {
            @JsonProperty("formatted_address")
            private String formattedAddress;
            private GoogleGeometry geometry;
        }

        @Getter
        @ToString
        @NoArgsConstructor
        static class GoogleGeometry {
            private Location location;
        }

        @Getter
        @ToString
        @NoArgsConstructor
        static class Location {
            private double lat;
            private double lng;
        }
    }
}
