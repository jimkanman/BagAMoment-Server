package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.*;
import com.jkm.jimkanman.global.success.SuccessResponse;
import com.jkm.jimkanman.service.DeliveryService;
import com.jkm.jimkanman.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "배송 API", description = "배송 관련 API입니다")
@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final StorageService storageService;

    /**
     * 필요 API 목록
     * - 배송 신청하기
     * - 배송 전담하기 (배달기사 배정)
     * - 최근 등록된 배송 조회
     * - 배송 현재 위치 가져오기
     * - 배송 시작하기 (웹소켓 or HTTP)
     * - 배송 위치 업데이트
     * - 배송 끝내기
     * */

    /** 배송 신청하기 */
    @Operation(summary = "배송 예약 (배송 버튼은 이 API 호출하기)", description = "입력받은 정보로 보관소를 예약 + 배송까지 예약함")
    @PostMapping("/storages/{storageId}/delivery-reservations")
    public ResponseEntity<SuccessResponse<ReservationResponse.ReservationDto>> registerDelivery(
            @PathVariable("storageId")Long storageId,
            @Valid @RequestBody DeliveryRequest.ReservationDto reservationDto
    ) {
        // 예약 생성
        ReservationResponse.ReservationResultDto reservationResult
                = storageService.makeReservation(storageId, new ReservationRequest.ReservationDto(reservationDto));

        // 배송예약 & 배송 객체 생성
        ReservationResponse.ReservationDto deliveryReservationResult = deliveryService.makeDeliveryReservation(reservationResult.getId(), reservationDto);
        return SuccessResponse.ok(deliveryReservationResult);
    }

    /** 배송기사 배정하기 */
    @Operation(summary = "배송 기사 배정 (배송 앱에서 사용)", description = "해당 id의 배송을 assigned 처리함")
    @PostMapping("/delivery/assign/{deliveryId}")
    public ResponseEntity<SuccessResponse<DeliveryResponse.SimpleDeliveryDto>> assignDelivery(@PathVariable("deliveryId") Long deliveryId) {
        //배송 객체 status 업데이트 -> ASSIGNED
        DeliveryResponse.SimpleDeliveryDto simpleDeliveryDto = deliveryService.assignDelivery(deliveryId);
        return SuccessResponse.ok(simpleDeliveryDto); // 배송 id 반환
    }

    /** PENDING 상태인 최근 배송 조회  */
    @Operation(summary = "배송 요청 조회 (배송 앱에서 사용)", description = "신청 가능한 배송 요청 목록을 가져옴")
    @GetMapping("/delivery")
    public ResponseEntity<SuccessResponse<List<DeliveryResponse.DeliveryDto>>> getRecentDeliveries() {
        // 배송 예약 객체 created_at 기준 정렬 후 조회
        List<DeliveryResponse.DeliveryDto> pendingDeliveries = deliveryService.getPendingDeliveries();
        return SuccessResponse.ok(pendingDeliveries);
    }

    /** 특정 배송 조회 */
    @Operation(summary = "배송 조회", description = "특정 ID의 배송 정보 조회")
    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<SuccessResponse<DeliveryResponse.DeliveryDto>> getDelivery(@PathVariable("deliveryId") Long deliveryId) {
        DeliveryResponse.DeliveryDto delivery = deliveryService.findDeliveryById(deliveryId);
        return SuccessResponse.ok(delivery);
    }

    /** 배송 시작하기 */
    @Operation(summary = "배송 시작 (배송 앱에서 사용)", description = "해당 id의 배송 시작 처리")
    @PostMapping("/delivery/{deliveryId}/location")
    public ResponseEntity<SuccessResponse<Object>> startDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) {
        // 배송 객체 status 업데이트 + 요청받은 위치로 배송 객체 초기화
        deliveryService.startDelivery(deliveryId, latitude, longitude);
        return SuccessResponse.ok(null);
    }

    @Operation(summary = "배송 위치 수정 (HTTP Polling)", description = "입력받은 위치로 배송 위치를 수정함")
    @PutMapping("/delivery/{deliveryId}/location")
    public ResponseEntity<SuccessResponse<?>> updateDeliveryLocation() {
        // polling 방식

        return null;
    }

    @Operation(summary = "배송 위치 조회 (HTTP Polling)", description = "해당 id 배송의 위치를 조회함")
    @GetMapping("/delivery/{deliveryId}/location")
    public ResponseEntity<SuccessResponse<LocationDto>> getDeliveryLocation(@PathVariable("deliveryId") Long deliveryId) {
        LocationDto location = deliveryService.getDeliveryLocation(deliveryId);
        return SuccessResponse.ok(location);
    }

    public ResponseEntity<SuccessResponse<?>> endDelivery() {
        // 배송 객체 status 업데이트 + 기타 필요 작업 (웹소켓 끊는다던가?)
        return null;
    }

}
