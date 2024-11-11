package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.StorageRequest;
import com.jkm.jimkanman.dto.StorageResponse;
import com.jkm.jimkanman.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "보관소 API", description = "보관소 관련 API입니다.")
public class StorageController {
    private final StorageService storageService;

    @Operation(summary = "보관소 등록", description = "입력받은 정보로 보관소를 등록함")
    @PostMapping("/storages")
    public Object saveStorage(@ModelAttribute StorageRequest.StorageRegisterDto registerDto) {
        // TODO
//        StorageResponse.StorageDto storageDto = storageService.saveStorage();
        return null;
    }

    @Operation(summary = "보관소 예약", description = "입력받은 정보로 보관소를 예약함")
    @PostMapping("/storages/{storageId}/reservations")
    public Object reserveStorage(@PathVariable("storageId") String storageId) {
        // TODO
        return null;
    }

    @Operation(summary = "보관소 예약 확인", description = "해당 id의 보관소에 걸린 예약 목록을 가져옴")
    @GetMapping("/storages/{storageId}/reservations")
    public Object getStorageReservations(@PathVariable("storageId") String storageId) {
        // TODO
        return null;
    }

    @Operation(summary = "보관소 상세 정보", description = "해당 id의 보관소의 상세 정보를 가져옴")
    @GetMapping("/storages/{storageId}")
    public Object getStorage(@PathVariable("storageId") String storageId) {
        // TODO
        return null;
    }

    @Operation(summary = "근처 보관소 탐색", description = "탐색 반경 내에 있는 보관소 목록을 가져옴")
    @GetMapping("/storages/nearby")
    public Object findNearbyStorages(@RequestParam Double latitude,
                                     @RequestParam Double longitude,
                                     @RequestParam(required = false, defaultValue = "1000") Integer radius) {
        // 짐 상태도 받아야 하나? -> 보관소에서 받을지 안받을지 설정이 없으므로 일단 받는다
        System.out.println("StorageController: findNearbyStorages at " + latitude + ", " + longitude + ", radius = " + radius);
//        List<StorageResponse.StorageDto> nearbyStorages = storageService.findNearbyStorages(latitude, longitude, radius);
//        return ResponseEntity.ok(nearbyStorages);
        return null;
    }

    @Operation(summary = "예약 정보 확인", description = "해당 id의 예약 정보를 가져옴")
    @GetMapping("/reservations/{reservationId}")
    public Object getReservation(@PathVariable("reservationId") String reservationId) {
        // TODO
        return null;
    }



}
