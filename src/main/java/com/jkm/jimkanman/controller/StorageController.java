package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.ReservationRequest;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.dto.StorageRequest;
import com.jkm.jimkanman.dto.StorageResponse;
import com.jkm.jimkanman.global.success.SuccessResponse;
import com.jkm.jimkanman.service.StorageService;
import com.jkm.jimkanman.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "보관소 API", description = "보관소 관련 API입니다.")
public class StorageController {
    private final StorageService storageService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "보관소 등록", description = "입력받은 정보로 보관소를 등록함")
    @PostMapping("/storages")
    public ResponseEntity<SuccessResponse<StorageResponse.StorageDto>> saveStorage(@ModelAttribute StorageRequest.StorageRegisterDto registerDto) {
        StorageResponse.StorageDto storageDto = storageService.save(registerDto);
        return SuccessResponse.ok(storageDto);
    }

    @Operation(summary = "보관소 예약", description = "입력받은 정보로 보관소를 예약함")
    @PostMapping("/storages/{storageId}/reservations")
    public ResponseEntity<SuccessResponse<ReservationResponse.ReservationResultDto>> reserveStorage(@PathVariable("storageId") Long storageId,
                                 @Valid @RequestBody ReservationRequest.ReservationDto reservationDto) {
        ReservationResponse.ReservationResultDto reservationResultDto = storageService.makeReservation(storageId, reservationDto);
        return SuccessResponse.ok(reservationResultDto);
    }

    @Operation(summary = "보관소 예약 확인", description = "해당 id의 보관소에 걸린 예약 목록을 가져옴")
    @GetMapping("/storages/{storageId}/reservations")
    public ResponseEntity<SuccessResponse<List<ReservationResponse.ReservationDto>>> getStorageReservations(@PathVariable("storageId") Long storageId) {
        List<ReservationResponse.ReservationDto> reservationDtos = storageService.findReservationsOnStorage(storageId, securityUtil.getRequiredMemberId());
        return SuccessResponse.ok(reservationDtos);
    }

    @Operation(summary = "보관소 상세 정보", description = "해당 id의 보관소의 상세 정보를 가져옴")
    @GetMapping("/storages/{storageId}")
    public ResponseEntity<SuccessResponse<StorageResponse.StorageDto>> getStorage(@PathVariable("storageId") String storageId) {
        StorageResponse.StorageDto storageDto = storageService.findById(Long.parseLong(storageId));
        return SuccessResponse.ok(storageDto);
    }

    @Operation(summary = "근처 보관소 탐색", description = "탐색 반경 내에 있는 보관소 목록을 가져옴")
    @GetMapping("/storages/nearby")
    public ResponseEntity<SuccessResponse<List<StorageResponse.StoragePreviewDto>>> findNearbyStorages(@RequestParam Double latitude,
                                                                                                       @RequestParam Double longitude,
                                                                                                       @RequestParam(required = false, defaultValue = "1000") Integer radius) {
        System.out.println("StorageController: findNearbyStorages at " + latitude + ", " + longitude + ", radius = " + radius);
        List<StorageResponse.StoragePreviewDto> nearbyStorages = storageService.findNearbyStorages(latitude, longitude, radius);
        return SuccessResponse.ok(nearbyStorages);
    }

    @Operation(summary = "예약 정보 확인", description = "해당 id의 예약 정보를 가져옴")
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<SuccessResponse<ReservationResponse.ReservationDto>> getReservation(@PathVariable("reservationId") Long reservationId) {
        // TODO
        ReservationResponse.ReservationDto reservationDto = storageService.findReservationById(reservationId);
        return SuccessResponse.ok(reservationDto);
    }



}
