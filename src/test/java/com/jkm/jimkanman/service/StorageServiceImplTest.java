package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Storage;
import com.jkm.jimkanman.dto.StorageResponse;
import com.jkm.jimkanman.repository.StorageRepository;
import com.jkm.jimkanman.util.GpsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {
    @Mock
    private StorageRepository storageRepository;

    @Mock
    private GpsUtil gpsUtil;

    @InjectMocks
    private StorageServiceImpl storageService;

    @Test
    void 근처_보관소_탐색() {
        /** 단위 테스트 */
        // Given 
        // 현재 위치
        Double latitude = 37.5665;
        Double longitude = 126.9780;
        Integer radiusKm = 5;
        
        // 주변 위치
        double minLat = 37.5000, maxLat = 37.6000;
        double minLng = 126.9000, maxLng = 127.0000;

        // Mock GpsUtil
        Mockito.when(gpsUtil.calculateLatLngRangeAroundTarget(latitude, longitude, radiusKm))
                .thenReturn(new double[]{minLat, maxLat, minLng, maxLng});
        Mockito.when(gpsUtil.isWithinRadius(latitude, longitude, 37.5500, 126.9500, radiusKm))
                .thenReturn(true);
        Mockito.when(gpsUtil.isWithinRadius(latitude, longitude, 37.6100, 127.1000, radiusKm))
                .thenReturn(false);

        // Mock Repository
        List<Storage> mockStorages = List.of(
                Storage.builder() // 반경 내 보관소
                        .id(1L)
                        .name("Storage1")
                        .latitude(37.5500)
                        .longitude(126.9500).
                        build(),
                Storage.builder() // 반경 외 보관소
                        .id(2L)
                        .name("Storage2")
                        .latitude(37.6100)
                        .longitude(127.1000)
                        .build()
        );
        Mockito.when(storageRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng))
                .thenReturn(mockStorages);



        // When
        List<StorageResponse.SimpleStorageDto> result = storageService.findNearbyStorages(latitude, longitude, radiusKm);

        // Then
        assertEquals(1, result.size());
        assertEquals("Storage1", result.get(0).getName());
    }
}