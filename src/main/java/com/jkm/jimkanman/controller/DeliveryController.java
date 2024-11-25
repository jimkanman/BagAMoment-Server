package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.ReservationRequest;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.global.success.SuccessResponse;
import com.jkm.jimkanman.service.DeliveryService;
import com.jkm.jimkanman.service.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<SuccessResponse<?>> registerDelivery(
            @PathVariable("storageId")Long storageId,
            @Valid @RequestBody DeliveryRequest.ReservationDto reservationDto
    ) {
        // 예약 & 배송 예약 생성
        // 배송 객체도 만들어둔 후 status PENDING으로 남김

        // 예약 생성
        ReservationResponse.ReservationResultDto reservationResult
                = storageService.makeReservation(storageId, new ReservationRequest.ReservationDto(reservationDto));

        // 배송예약 & 배송 객체 생성
        deliveryService.makeDeliveryReservation(reservationResult.getId(), reservationDto);
        return null;
    }

    /** 배송기사 배정하기 */
    public ResponseEntity<SuccessResponse<?>> assignDelivery(@PathVariable("deliveryId") Long deliveryId) {
        //배송 객체 status 업데이트 -> ASSIGNED
        deliveryService.assignDelivery(deliveryId);
        // id 반환
        return null;
    }

    /** PENDING 상태인 최근 배송 조회  */
    public ResponseEntity<SuccessResponse<?>> getRecentDeliveries() {
        // 배송 예약 객체 created_at 기준 정렬 후 조회
        deliveryService.getPendingDeliveries();
        return null;
    }

    /** 배송 시작하기 */
    public ResponseEntity<SuccessResponse<?>> startDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) {
        // 배송 객체 status 업데이트 + 요청받은 위치로 배송 객체 초기화
        deliveryService.startDelivery(deliveryId, latitude, longitude);
        return null;
    }

    public ResponseEntity<SuccessResponse<?>> updateDeliveryLocation() {
        // polling 방식
        
        return null;
    }

    public ResponseEntity<SuccessResponse<?>> getDeliveryLocation() {
        return null;
    }

    public ResponseEntity<SuccessResponse<?>> endDelivery() {
        // 배송 객체 status 업데이트 + 기타 필요 작업 (웹소켓 끊는다던가?)
        return null;
    }

}
