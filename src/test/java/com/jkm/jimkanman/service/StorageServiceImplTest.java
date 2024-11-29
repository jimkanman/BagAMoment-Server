package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Coordinate;
import com.jkm.jimkanman.domain.Member;
import com.jkm.jimkanman.domain.Storage;
import com.jkm.jimkanman.domain.StorageRegistration;
import com.jkm.jimkanman.domain.enums.StorageOption;
import com.jkm.jimkanman.dto.MemberResponse;
import com.jkm.jimkanman.dto.StorageRequest;
import com.jkm.jimkanman.dto.StorageResponse;
import com.jkm.jimkanman.dto.StorageResponse.StorageDto;
import com.jkm.jimkanman.repository.MemberRepository;
import com.jkm.jimkanman.repository.StorageImageRepository;
import com.jkm.jimkanman.repository.StorageRegistrationRepository;
import com.jkm.jimkanman.repository.StorageRepository;
import com.jkm.jimkanman.util.GpsUtil;
import com.jkm.jimkanman.util.SecurityUtil;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {
    @Mock
    private StorageRepository storageRepository;

    @Mock
    private StorageRegistrationRepository storageRegistrationRepository;

    @Mock
    private StorageImageRepository storageImageRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private FileService fileService;

    @InjectMocks
    private StorageServiceImpl storageService;

    @Test
    void 보관소_생성() {
        // given
        String termsFilePath = "/path/to/terms.pdf";
        Member owner = Member.builder()
                .id(1L)
                .nickname("Test User")
                .storages(new ArrayList<>()) // 초기화 추가
                .build();
        Coordinate coordinate = new Coordinate(37.5665, 126.9780); // 서울의 위도, 경도
        StorageRequest.StorageRegisterDto registerDto = StorageRequest.StorageRegisterDto.builder()
                .registerName("테스트 보관소")
                .phoneNumber("010-1234-5678")
                .description("테스트 보관소 설명")
                .postalCode("12345")
                .detailedAddress("서울특별시 중구 세종대로 110")
                .openingTime("09:00")
                .closingTime("18:00")
                .backpackPricePerHour(1000)
                .carrierPricePerHour(2000)
                .miscellaneousItemPricePerHour(500)
                .storageOptions(List.of("CCTV","CART"))
                .termsAndConditions(mock(MultipartFile.class))
                .storageImages(List.of(mock(MultipartFile.class), mock(MultipartFile.class)))
                .build();



        // when: 의존성 목(mock) 설정
        given(fileService.saveFile(any())).willReturn(termsFilePath);
        given(gpsUtil.convertToCoordinates(anyString())).willReturn(coordinate);
        given(securityUtil.getRequiredMemberId()).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(owner));
        given(storageRepository.save(any(Storage.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(storageRegistrationRepository.save(any(StorageRegistration.class))).willAnswer(invocation -> invocation.getArgument(0));

        // 실행
        StorageResponse.StorageDto response = storageService.save(registerDto);

        // then: 결과 검증
        assertNotNull(response);
        assertEquals(registerDto.getRegisterName(), response.getName());
        assertEquals(registerDto.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(registerDto.getDetailedAddress(), response.getDetailedAddress());
        assertEquals(coordinate.getLatitude(), response.getLatitude());
        assertEquals(coordinate.getLongitude(), response.getLongitude());

        // 목 검증
        verify(fileService, times(1)).saveFile(registerDto.getTermsAndConditions());
        verify(gpsUtil, times(1)).convertToCoordinates(registerDto.getDetailedAddress());
        verify(storageRepository, times(1)).save(any(Storage.class));
        verify(storageRegistrationRepository, times(1)).save(any(StorageRegistration.class));
    }

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
                        .longitude(126.9500)
                        .openingTime("09:00")
                        .closingTime("18:00")
                        .build(),
                Storage.builder() // 반경 외 보관소
                        .id(2L)
                        .name("Storage2")
                        .latitude(37.6100)
                        .longitude(127.1000)
                        .openingTime("09:00")
                        .closingTime("18:00")
                        .build()
        );
        Mockito.when(storageRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng))
                .thenReturn(mockStorages);



        // When
        List<StorageResponse.StoragePreviewDto> result = storageService.findNearbyStorages(latitude, longitude, radiusKm);

        // Then
        assertEquals(1, result.size());
        assertEquals("Storage1", result.get(0).getName());
    }
    @Test
    void 보관소_검색_범위내() {
        /** 단위 테스트 */
        // Given
        // 현재 위치
        Double latitude = 37.5665;
        Double longitude = 126.9780;
        Integer radiusKm = 5;
        String searchTerm = "Storage";

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
        List<Storage> mockStoragesContainsSearchTerm = List.of(
                Storage.builder() // 반경 내 보관소, 검색어 포함
                        .id(1L)
                        .name("Storage1")
                        .latitude(37.5500)
                        .longitude(126.9500)
                        .openingTime("09:00")
                        .closingTime("18:00")
                        .build(),
                Storage.builder() // 반경 외 보관소, 검색어 포함
                        .id(2L)
                        .name("Storage2")
                        .latitude(37.6100)
                        .longitude(127.1000)
                        .openingTime("09:00")
                        .closingTime("18:00")
                        .build()
        );
        Mockito.when(storageRepository.findByNameContaining(searchTerm))
                .thenReturn(mockStoragesContainsSearchTerm);
        // When
        List<StorageResponse.StoragePreviewDto> result = storageService.findStoragesBySearchTerms(latitude, longitude, radiusKm,searchTerm);

        // Then
        assertEquals(1, result.size());
        assertEquals("Storage1", result.get(0).getName());
    }
}