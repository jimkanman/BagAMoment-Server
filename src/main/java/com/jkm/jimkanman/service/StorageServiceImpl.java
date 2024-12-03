package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.*;
import com.jkm.jimkanman.domain.enums.StorageRegistrationStatus;
import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import com.jkm.jimkanman.dto.*;
import com.jkm.jimkanman.dto.StorageResponse.StoragePreviewDto;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import com.jkm.jimkanman.repository.*;
import com.jkm.jimkanman.util.GpsUtil;
import com.jkm.jimkanman.util.SecurityUtil;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberRepository memberRepository;

    private final GpsUtil gpsUtil;
    private final FileService fileService;
    private final SecurityUtil securityUtil;

    @Override
    public List<StorageResponse.StoragePreviewDto> findNearbyStorages(Double latitude, Double longitude, Integer radiusKm) {
        // 해당 반경 내에 있는 보관소를 담아 영업중인 보관소와 영업 종료된 보관소로 나누고 거리순으로 반환
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

        // Stream 처리 전에 영업 상태와 거리를 계산해서 저장
        List<StoragePreviewDto> storageDtos = nearbyStorages.stream()
                .filter(storage -> gpsUtil.isWithinRadius(latitude, longitude, storage.getLatitude(), storage.getLongitude(), radiusKm))
                .map(storage -> {
                    double distance = gpsUtil.calculateDistance(latitude, longitude, storage.getLatitude(), storage.getLongitude());
                    LocalTime opening = LocalTime.parse(storage.getOpeningTime());
                    LocalTime closing = LocalTime.parse(storage.getClosingTime());
                    LocalTime now = LocalTime.now();
                    boolean isOpen = now.isAfter(opening) && now.isBefore(closing);
                    return new StorageResponse.StoragePreviewDto(storage, distance, isOpen);
                })
                .toList();
        // 영업 중인 보관소와 영업 종료된 보관소로 나누기
        List<StoragePreviewDto> openStorages = storageDtos.stream()
                .filter(StoragePreviewDto::getIsOpen) // 영업 중인 것만 선택
                .sorted(Comparator.comparingDouble(StoragePreviewDto::getDistance)) // 거리순 정렬
                .collect(Collectors.toList());

        List<StoragePreviewDto> closedStorages = storageDtos.stream()
                .filter(dto->!dto.getIsOpen()) // 영업 종료된 것만 선택
                .sorted(Comparator.comparingDouble(StoragePreviewDto::getDistance)) // 거리순 정렬
                .collect(Collectors.toList());

        // 두 리스트 합치기
        openStorages.addAll(closedStorages);
        return openStorages;
    }

    @Override
    public List<StoragePreviewDto> findStoragesBySearchTerms(Double latitude, Double longitude, Integer radiusKm,
                                                             String searchTerm) {
        // 해당 반경 내에 있는 보관소를 담아 반환
        if (latitude == null || longitude == null || radiusKm == null||searchTerm.isEmpty()) {
            throw new IllegalArgumentException("위도와 경도, 반경,검색어가 모두 제공되어야 합니다.");
        }
        double[] targetRange = gpsUtil.calculateLatLngRangeAroundTarget(latitude, longitude, radiusKm);
        double minLat = targetRange[0];
        double maxLat = targetRange[1];
        double minLng = targetRange[2];
        double maxLng = targetRange[3];
        // 데이터베이스에서 검색어 기반 보관소 조회
        List<Storage> storagesBySearchTerm = storageRepository.findByNameContaining(searchTerm);

        // 거리 필터링하여 반경 내에 있는 보관소만 선택
        return storagesBySearchTerm.stream()
                .filter(storage -> gpsUtil.isWithinRadius(latitude, longitude, storage.getLatitude(), storage.getLongitude(), radiusKm))
                .map(storage -> {
                    double distance = gpsUtil.calculateDistance(latitude, longitude, storage.getLatitude(), storage.getLongitude());
                    LocalTime opening = LocalTime.parse(storage.getOpeningTime());
                    LocalTime closing = LocalTime.parse(storage.getClosingTime());
                    LocalTime now = LocalTime.now();
                    boolean isOpen = now.isAfter(opening) && now.isBefore(closing);
                    return new StorageResponse.StoragePreviewDto(storage, distance, isOpen);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StorageResponse.StorageDto save(StorageRequest.StorageRegisterDto registerDto) {
        // 주소에서 위도와 경도를 얻어옴
        Coordinate coordinate;
        try {
            coordinate = gpsUtil.convertToCoordinates(registerDto.getDetailedAddress());
        } catch (Exception e){
            System.out.println("StorageService: exception while converting address to coordinate; " + e.getMessage());
            System.out.println("StorageService: setting coordinate for storage '" + registerDto.getRegisterName() +"' to (null, null)");
            coordinate = new Coordinate(null, null);
        }

        // 약관 파일 저장
        String termsAndConditionsFilePath = fileService.saveFile(registerDto.getTermsAndConditions());

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
                .termsAndConditions(termsAndConditionsFilePath)
                .storageImages(new ArrayList<>())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
        Member owner = memberRepository.findById(securityUtil.getRequiredMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        storage.setOwner(owner);

        // 이미지 파일 저장
        List<StorageImage> images = new ArrayList<>();
        if(registerDto.getStorageImages() != null) {
            images = registerDto.getStorageImages().stream()
                    .map(file -> {
                        String filePath = fileService.saveFile(file);
                        StorageImage storageImage = StorageImage.builder()
                                .originalFileName(file.getOriginalFilename())
                                .storedFileName(filePath)
                                .build();
                        storageImage.setStorage(storage);
                        return storageImage;
                    }).toList();
        }

        // 보관소 예약 생성
        StorageRegistration registration = StorageRegistration.builder()
                .storage(storage)
                .member(owner)
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
        Storage storage = storageRepository.findById(storageId).orElseThrow( () -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND));
        return new StorageResponse.StorageDto(storage);
    }

    @Override
    public ReservationResponse.ReservationResultDto makeReservation(Long storageId, ReservationRequest.ReservationDto reservationDto) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND));

        // 예약 시간 확인
        LocalDateTime startDateTime, endDateTime;
        startDateTime = LocalDateTime.parse(reservationDto.getStartDateTime());
        endDateTime = LocalDateTime.parse(reservationDto.getEndDateTime());
        if(!isWithinReservationTime(startDateTime, storage.getOpeningTime(), storage.getClosingTime())
            || !isWithinReservationTime(endDateTime, storage.getOpeningTime(), storage.getClosingTime())){
            throw new BusinessException(ErrorCode.STORAGE_NOT_OPEN);
        }

        // 짐 변환 및 가격 계산
        if(reservationDto.getLuggage() == null) throw new BusinessException(ErrorCode.LUGGAGE_NULL);
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
                .luggageList(new ArrayList<>())
                .paymentAmount(price)
                .status(StorageReservationStatus.PENDING) // 예약 상태 대기 중으로 초기화
                .build();

        luggages.forEach(luggage -> luggage.setReservation(reservation));

        // 저장
        StorageReservation savedReservation = storageReservationRepository.save(reservation);
        luggageRepository.saveAll(luggages);

        return new ReservationResponse.ReservationResultDto(savedReservation);
    }

    @Override
    public List<ReservationResponse.ReservationDto> findReservationsOnStorage(Long storageId) {
        List<StorageReservation> reservations = storageReservationRepository.findAllByStorageId(storageId);
        return reservations.stream()
                .map(reservation -> new ReservationResponse.ReservationDto(reservation))
                .toList();
    }

    @Override
    public List<ReservationResponse.ReservationDto> findReservationsOnStorage(Long storageId, Long memberId) {
        List<StorageReservation> reservations = storageReservationRepository.findAllByStorageIdAndMemberIdOrderByEndDateTimeAsc(storageId, memberId);
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

    @Override
    @Transactional
    public List<ReservationResponse.ReservationPreviewDto> findReservationsByMemberId(Long userId) {
        List<StorageReservation> reservations = storageReservationRepository.findAllByMemberId(userId);
        return reservations.stream()
                .map(reservation -> {
                    if(reservation.getStorage().getStorageImages().isEmpty())
                        return new ReservationResponse.ReservationPreviewDto(reservation);
                    else
                        return new ReservationResponse.ReservationPreviewDto(
                                reservation,
                                reservation.getStorage().getStorageImages().get(0).getStoredFileName());
                })
                .toList();
    }

    private boolean isWithinReservationTime(LocalDateTime reservationDateTime, String openingTime, String closingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime reservationTime = reservationDateTime.toLocalTime();
        // 보관소 예약 시간(LocalTime)으로 변환
        LocalTime opening = LocalTime.parse(openingTime, formatter);
        LocalTime closing = LocalTime.parse(closingTime, formatter);

        // 예약 시간이 보관소 시간 범위 내에 있는지 확인
        return !reservationTime.isBefore(opening) && !reservationTime.isAfter(closing);
    }
}
