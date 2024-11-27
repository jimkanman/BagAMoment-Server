package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Coordinate;
import com.jkm.jimkanman.domain.Delivery;
import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.StorageReservation;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.DeliveryResponse;
import com.jkm.jimkanman.dto.LocationDto;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import com.jkm.jimkanman.repository.DeliveryRepository;
import com.jkm.jimkanman.repository.DeliveryReservationRepository;
import com.jkm.jimkanman.repository.StorageReservationRepository;
import com.jkm.jimkanman.util.GpsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final StorageReservationRepository storageReservationRepository;
    private final DeliveryReservationRepository deliveryReservationRepository;
    private final DeliveryRepository deliveryRepository;

    private final GpsUtil gpsUtil;

    /** 배송 예약 생성 */
    @Override
    public ReservationResponse.ReservationDto makeDeliveryReservation(Long reservationId, DeliveryRequest.ReservationDto reservationDto) {
        // 배송 지점 위도 경도 계산
        Coordinate coordinate;
        try {
            coordinate = gpsUtil.convertToCoordinates(reservationDto.getDestinationAddress());
        } catch (Exception e) {
            coordinate = new Coordinate(0d, 0d);
        }

        // 배송 객체, 배송 예약 객체 생성 후 보관소 예약 객체와 관계 맺어 저장
        StorageReservation storageReservation = storageReservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    System.out.println("DeliveryService: Exception - storage reservation with id " + reservationId + " not found");
                    throw new BusinessException(ErrorCode.STORAGE_RESERVATION_NOT_FOUND);
                });

        DeliveryReservation deliveryReservation = DeliveryReservation.builder()
                .deliveryArrivalDateTime(LocalDateTime.parse(reservationDto.getDeliveryArrivalDateTime())) // todo
                .destinationAddress(reservationDto.getDestinationAddress())
                .destinationPostalCode(reservationDto.getDestinationPostalCode())
                .destinationLatitude(coordinate.getLatitude())
                .destinationLongitude(coordinate.getLongitude())
                .build();
        deliveryReservation.setStorageReservation(storageReservation);

        Delivery delivery = Delivery.builder()
                .status(DeliveryStatus.PENDING)
                .build();
        delivery.setDeliveryReservation(deliveryReservation);

        // 저장 (Cascade.ALL로 결합됐으므로 부모 엔티티만 저장해도 됨)
        StorageReservation savedStorageReservation = storageReservationRepository.save(storageReservation);

        ReservationResponse.ReservationDto resultDto = new ReservationResponse.ReservationDto(savedStorageReservation);
        return resultDto;
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryResponse.SimpleDeliveryDto assignDelivery(Long deliveryId) {
        // TODO N+1 쿼리 나가는지 확인 -> 맞는 경우 Fetch join으로 대체 (Delivery - DeliveryReservation - StorageReservation join)
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.setStatus(DeliveryStatus.ON_DELIVERY);
        return new DeliveryResponse.SimpleDeliveryDto(delivery);
    }

    @Override
    public List<DeliveryResponse.DeliveryDto> getPendingDeliveries() {
        // Pending 상태인 배송 예약 created_at 기준 정렬 후 조회
        List<Delivery> deliveries = deliveryRepository.findAllByOrderByCreatedAtDesc();
        List<DeliveryResponse.DeliveryDto> deliveryDtos = deliveries.stream()
                .map(delivery -> new DeliveryResponse.DeliveryDto(delivery))
                .toList();
        return deliveryDtos;
    }

    @Override
    public Object startDelivery(Long deliveryId, Double latitude, Double longitude) {
        // Delivery 시작: status 업데이트 + 위치로 배송 객체 초기화
        
        return null;
    }

    @Override
    public void updateDeliveryLocation(LocationDto request) {
        System.out.println("DeliveryService: received (id=" + request.getDeliveryId() + ", lat=" + request.getLatitude() + ", lng=" + request.getLongitude() + ")");
        if(request.getLatitude() == null || request.getLongitude() == null){
            System.out.println("DeliveryService: warning - location update request to " + request.getDeliveryId() + " is null");

            return;
        }
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.setLatitude(request.getLatitude());
        delivery.setLongitude(request.getLongitude());
        deliveryRepository.save(delivery);
    }

    @Override
    public LocationDto getDeliveryLocation(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        return new LocationDto(delivery);
    }
}
