package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.*;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.DeliveryResponse;
import com.jkm.jimkanman.dto.LocationDto;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.global.error.ErrorCode;
import com.jkm.jimkanman.global.error.exception.BusinessException;
import com.jkm.jimkanman.repository.DeliveryRepository;
import com.jkm.jimkanman.repository.DeliveryReservationRepository;
import com.jkm.jimkanman.repository.StorageRepository;
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
    private final StorageRepository storageRepository;

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
                .status(DeliveryStatus.PENDING)
                .build();
        deliveryReservation.setStorageReservation(storageReservation);

        Delivery delivery = Delivery.builder()
//                .status(DeliveryStatus.PENDING)
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
        delivery.getDeliveryReservation().setStatus(DeliveryStatus.ASSIGNED);
//        deliveryRepository.save(delivery); // TODO 주석해제
        return new DeliveryResponse.SimpleDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public List<DeliveryResponse.ReservationDto> getPendingDeliveries() {
        // Pending 상태인 배송 예약 created_at 기준 정렬 후 조회
//       List<DeliveryReservation> deliveryReservations = deliveryReservationRepository.findAllByOrderByCreatedAtDesc(); // delivery - deliveryReservation - storageReservation 다 한번에 fetch해서 가져오게 하고 싶은데?
        List<DeliveryReservation> deliveryReservations = deliveryReservationRepository.findAllWithDeliveryAndStorageOrderByCreatedAtDesc();
        List<DeliveryResponse.ReservationDto> reservationDtos = deliveryReservations.stream()
//                .filter(reservation -> reservation.getDelivery().getStatus().equals(DeliveryStatus.PENDING))
                .filter(reservation -> reservation.getStatus().equals(DeliveryStatus.PENDING))
                .map(reservation -> new DeliveryResponse.ReservationDto(reservation))
                .toList();
        // 출발지 ~ 목적지 거리 계산
        reservationDtos.forEach(reservation -> {
            try {
                Double distance = gpsUtil.calculateDistance(
                        reservation.getDestinationLatitude(),
                        reservation.getDestinationLongitude(),
                        reservation.getStorageLatitude(),
                        reservation.getStorageLongitude());
                reservation.setDistance(distance);
            } catch (Exception e) {
                System.out.println("DeliveryService: EXCEPTION at getPendingDeliveries while calculating distance = " + e.getMessage());
            }
        });
        return reservationDtos;
    }

    @Override
    @Transactional
    public Object startDelivery(Long deliveryId, Double latitude, Double longitude) {
        // Delivery 시작: status 업데이트 + 위치로 배송 객체 초기화
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.getDeliveryReservation().setStatus(DeliveryStatus.ON_DELIVERY);
        if (latitude != null && longitude != null) {
            delivery.setLatitude(latitude);
            delivery.setLongitude(longitude);
        }
        deliveryRepository.save(delivery);
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

    @Override
    public DeliveryResponse.DeliveryDto findDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        return new DeliveryResponse.DeliveryDto(delivery);
    }

    @Override
    public DeliveryResponse.ReservationDto findDeliveryReservationById(Long deliveryReservationId) {
        DeliveryReservation deliveryReservation = deliveryReservationRepository.findById(deliveryReservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_RESERVATION_NOT_FOUND));
        DeliveryResponse.ReservationDto reservationDto = new DeliveryResponse.ReservationDto(deliveryReservation);
        reservationDto.setDistance(gpsUtil.calculateDistance(
                reservationDto.getDestinationLatitude(),
                reservationDto.getDestinationLongitude(),
                reservationDto.getStorageLatitude(),
                reservationDto.getStorageLongitude()));
        return new DeliveryResponse.ReservationDto(deliveryReservation);
    }

    @Override
    @Transactional
    public void cancelDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.setLatitude(null);
        delivery.setLongitude(null);
        delivery.getDeliveryReservation().setStatus(DeliveryStatus.PENDING);
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public DeliveryResponse.DeliveryAndReservationDto endDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.getDeliveryReservation().setStatus(DeliveryStatus.COMPLETE);
        delivery.recordArrivalTime();
        deliveryRepository.save(delivery);

        return new DeliveryResponse.DeliveryAndReservationDto(delivery);
    }
}
