package com.jkm.jimkanman.service;

import com.jkm.jimkanman.dto.ReservationRequest;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.dto.StorageRequest;
import com.jkm.jimkanman.dto.StorageResponse;

import com.jkm.jimkanman.dto.StorageResponse.StoragePreviewDto;
import java.util.List;


public interface StorageService {

    List<StorageResponse.StoragePreviewDto> findNearbyStorages(Double latitude, Double longitude, Integer radius);

    StorageResponse.StorageDto save(StorageRequest.StorageRegisterDto registerDto);

    StorageResponse.StorageDto findById(Long storageId);

    ReservationResponse.ReservationResultDto makeReservation(Long storageId, ReservationRequest.ReservationDto reservationDto);

    List<ReservationResponse.ReservationDto> findReservationsOnStorage(Long storageId, Long memberId);

    List<ReservationResponse.ReservationDto> findReservationsOnStorage(Long storageId);

    ReservationResponse.ReservationDto findReservationById(Long reservationId);

    List<ReservationResponse.ReservationPreviewDto> findReservationsByMemberId(Long userId);

    List<StoragePreviewDto> findStoragesBySearchTerms(Double latitude, Double longitude, Integer radius, String searchTerm);

    boolean checkDeliveryService(Long storageId);

    List<StorageResponse.StorageDto> findAllByOwnerId(Long memberId);

    List<ReservationResponse.ReservationPreviewDto> findReservationsOnStoragesByOwnerId(Long memberId);
}
