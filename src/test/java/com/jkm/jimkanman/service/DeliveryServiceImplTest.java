package com.jkm.jimkanman.service;

import com.jkm.jimkanman.domain.Delivery;
import com.jkm.jimkanman.domain.DeliveryReservation;
import com.jkm.jimkanman.domain.StorageReservation;
import com.jkm.jimkanman.domain.enums.DeliveryStatus;
import com.jkm.jimkanman.domain.enums.StorageReservationStatus;
import com.jkm.jimkanman.dto.DeliveryRequest;
import com.jkm.jimkanman.dto.DeliveryResponse;
import com.jkm.jimkanman.dto.ReservationResponse;
import com.jkm.jimkanman.repository.DeliveryRepository;
import com.jkm.jimkanman.repository.DeliveryReservationRepository;
import com.jkm.jimkanman.repository.StorageReservationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class DeliveryServiceImplTest {

    @Autowired
    private DeliveryServiceImpl deliveryService;

    @MockBean
    private StorageReservationRepository storageReservationRepository;

    @MockBean
    private DeliveryReservationRepository deliveryReservationRepository;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @Test
    public void testGetPendingDeliveries() {
        // Given
        // 데이터 Mocking
        Delivery delivery1 = Delivery.builder()
                .id(1L)
//                .status(DeliveryStatus.PENDING)
                .build();

        StorageReservation storageReservation1 = StorageReservation.builder()
                .id(101L)
                .build();

        DeliveryReservation deliveryReservation1 = DeliveryReservation.builder()
                .id(201L)
                .delivery(delivery1)
                .storageReservation(storageReservation1)
                .status(DeliveryStatus.PENDING)
                .build();

        Delivery delivery2 = Delivery.builder()
                .id(2L)
//                .status(DeliveryStatus.COMPLETE)
                .build();

        StorageReservation storageReservation2 = StorageReservation.builder()
                .id(102L)
                .build();

        DeliveryReservation deliveryReservation2 = DeliveryReservation.builder()
                .id(202L)
                .delivery(delivery2)
                .storageReservation(storageReservation2)
                .status(DeliveryStatus.PENDING)
                .build();

        // Mock (Fetch Join으로 데이터 반환된다 가정)
        Mockito.when(deliveryReservationRepository.findAllWithDeliveryAndStorageOrderByCreatedAtDesc())
                .thenReturn(List.of(deliveryReservation1, deliveryReservation2));

        // When
        // Pending 상태인 예약 조회
        List<DeliveryResponse.ReservationDto> result = deliveryService.getPendingDeliveries();

        // Then
        Assertions.assertNotNull(result, "Result list should not be null");
        Assertions.assertEquals(1, result.size(), "Only one reservation with PENDING status should be returned");

        DeliveryResponse.ReservationDto dto = result.get(0);
        Assertions.assertEquals(201L, dto.getId(), "ReservationDto ID가 다릅니다.");
        Assertions.assertEquals(1L, dto.getDeliveryId(), "Delivery ID가 다릅니다.");
        Assertions.assertEquals(DeliveryStatus.PENDING, dto.getStatus(), "PENDING 상태가 아닙니다.");

        Mockito.verify(deliveryReservationRepository, Mockito.times(1)).findAllWithDeliveryAndStorageOrderByCreatedAtDesc();
    }
}