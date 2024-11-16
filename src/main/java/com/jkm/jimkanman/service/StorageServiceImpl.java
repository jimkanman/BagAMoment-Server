package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.*;
import com.jkm.jimkanman.domain.enums.StorageRegistrationStatus;
import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import com.jkm.jimkanman.dto.ReservationRequest;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.dto.StorageRequest;
import com.jkm.jimkanman.dto.StorageResponse;
import com.jkm.jimkanman.repository.*;
import com.jkm.jimkanman.util.GpsUtil;
import com.jkm.jimkanman.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final StorageRegistrationRepository storageRegistrationRepository;
    private final StorageImageRepository storageImageRepository;
    private final StorageReservationRepository storageReservationRepository;
    private final LuggageRepository luggageRepository;

    private final GpsUtil gpsUtil;
    private final FileService fileService;
    private final SecurityUtil securityUtil;

    @Override
    public List<StorageResponse.SimpleStorageDto> findNearbyStorages(Double latitude, Double longitude, Integer radiusKm) {
        // 해당 반경 내에 있는 보관소를 담아 반환
        if (latitude == null || longitude == null || radiusKm == null) {
            throw new IllegalArgumentException("위도와 경도, 반경이 모두 제공되어야 합니다.");
        }

        double[] targetRange = gpsUtil.calculateLatLngRangeAroundTarget(latitude, longitude, radiusKm);
        double minLat = targetRange[0];
        double maxLat = targetRange[1];
        double minLng = targetRange[2];
        double maxLng = targetRange[3];

        // 데이터베이스에서 범위 내 보관소 조회
        List<Storage> nearbyStorages = storageRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);

        // 거리 필터링하여 반경 내에 있는 보관소만 선택
        return nearbyStorages.stream()
                .filter(storage -> gpsUtil.isWithinRadius(latitude, longitude, storage.getLatitude(), storage.getLongitude(), radiusKm))
                .map(storage -> new StorageResponse.SimpleStorageDto(storage))
                .collect(Collectors.toList());
    }

    @Override
    public StorageResponse.StorageDto save(StorageRequest.StorageRegisterDto registerDto) {
        // 주소에서 위도와 경도를 얻어옴
        Coordinate coordinate;
        try {
            coordinate = gpsUtil.convertToCoordinates(registerDto.getDetailedAddress());
        } catch (Exception e){
            coordinate = new Coordinate(null, null);
        }

        // 약관 파일 저장
        String termsAndConditionsFilename = fileService.saveFile(registerDto.getTermsAndConditions());

        // 보관소 생성
        Storage storage = Storage.builder()
                .name(registerDto.getRegisterName())
                .phoneNumber(registerDto.getPhoneNumber())
                .description(registerDto.getDescription())
                .postalCode(registerDto.getPostalCode())
                .detailedAddress(registerDto.getDetailedAddress())
                .openingTime(registerDto.getOpeningTime())
                .closingTime(registerDto.getClosingTime())
                .backpackPricePerHour(registerDto.getBackpackPricePerHour())
                .carrierPricePerHour(registerDto.getCarrierPricePerHour())
                .miscellaneousItemPricePerHour(registerDto.getMiscellaneousItemPricePerHour())
                .termsAndConditions(termsAndConditionsFilename)
                .storageImages(new ArrayList<>())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();

        // 이미지 파일 저장
        List<StorageImage> images = registerDto.getStorageImages().stream()
                .map(file -> {
                    String filename = fileService.saveFile(file);
                    StorageImage storageImage = StorageImage.builder()
                            .originalFileName(file.getOriginalFilename())
                            .storedFileName(filename)
                            .build();
                    storageImage.setStorage(storage);
                    return storageImage;
                }).toList();

        // 보관소 예약 생성
        StorageRegistration registration = StorageRegistration.builder()
                .storage(storage)
                .member(securityUtil.getMember())
                .status(StorageRegistrationStatus.PENDING) // 등록 상태는 대기 중으로 설정
                .build();

        // 저장
        Storage savedStorage = storageRepository.save(storage);
        storageRegistrationRepository.save(registration);
        storageImageRepository.saveAll(images); // Storage에서 StorageImage를 CascadeType.ALL로 지정했으므로 필요 없긴 하지만.. 안 넣고 나중에 후회할 수도 있으니 넣음

        return new StorageResponse.StorageDto(savedStorage);
    }

    @Override
    public StorageResponse.StorageDto findById(Long storageId) {
        if(storageId == null) throw new RuntimeException("보관소 ID가 필요합니다.");
        Storage storage = storageRepository.findById(storageId).orElseThrow( () -> new RuntimeException("보관소를 찾을 수 없습니다."));
        return new StorageResponse.StorageDto(storage);
    }

    @Override
    public ReservationResponse.ReservationResultDto makeReservation(Long storageId, ReservationRequest.ReservationDto reservationDto) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new RuntimeException("보관소를 찾을 수 없습니다."));

        // 예약 시간 확인
        LocalDateTime startDateTime, endDateTime;
        startDateTime = LocalDateTime.parse(reservationDto.getStartDateTime());
        endDateTime = LocalDateTime.parse(reservationDto.getEndDateTime());
        if(!isWithinReservationTime(startDateTime, storage.getOpeningTime(), storage.getClosingTime())
            || !isWithinReservationTime(endDateTime, storage.getOpeningTime(), storage.getClosingTime())){
            throw new RuntimeException("예약시간이 보관소 이용시간과 맞지 않습니다.");
        }

        // 짐 변환 및 가격 계산
        List<Luggage> luggages = reservationDto.getLuggage().stream()
                .map(luggageDto -> Luggage.builder()
                            .type(luggageDto.getType())
                            .depth(luggageDto.getDepth())
                            .width(luggageDto.getWidth())
                            .height(luggageDto.getHeight())
                            .build())
                .toList();

        int price = luggages.stream()
                            .map(luggage -> switch (luggage.getType()) {
                                case BAG -> storage.getBackpackPricePerHour();
                                case CARRIER -> storage.getCarrierPricePerHour();
                                case MISCELLANEOUS_ITEM -> storage.getMiscellaneousItemPricePerHour();
                                default -> 0;
                            })
                            .mapToInt(i -> i)
                            .sum();

        // 예약 객체 생성
        StorageReservation reservation = StorageReservation.builder()
                .storage(storage)
                .member(securityUtil.getMember())
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .paymentAmount(price)
                .status(StorageReservationStatus.PENDING) // 예약 상태 대기 중으로 초기화
                .build();

        luggages.forEach(luggage -> luggage.setReservation(reservation));

        // 저장
        luggageRepository.saveAll(luggages);
        StorageReservation savedReservation = storageReservationRepository.save(reservation);

        return new ReservationResponse.ReservationResultDto(savedReservation);
    }

    @Override
    public List<ReservationResponse.ReservationDto> findReservationsOnStorage(Long storageId, Long memberId) {
        List<StorageReservation> reservations = storageReservationRepository.findByStorageIdAndMemberIdOrderByEndDateTimeAsc(storageId, memberId);
        return reservations.stream()
                .map(reservation -> new ReservationResponse.ReservationDto(reservation))
                .toList();
    }

    @Override
    public ReservationResponse.ReservationDto findReservationById(Long reservationId) {
        StorageReservation reservation = storageReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException(reservationId + " 에 해당하는 예약이 존재하지 않습니다."));
        return new ReservationResponse.ReservationDto(reservation);
    }

    public boolean isWithinReservationTime(LocalDateTime reservationDateTime, String openingTime, String closingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime reservationTime = reservationDateTime.toLocalTime();
        // 보관소 예약 시간(LocalTime)으로 변환
        LocalTime opening = LocalTime.parse(openingTime, formatter);
        LocalTime closing = LocalTime.parse(closingTime, formatter);

        // 예약 시간이 보관소 시간 범위 내에 있는지 확인
        return !reservationTime.isBefore(opening) && !reservationTime.isAfter(closing);
    }
}
