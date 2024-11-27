package com.jkm.jimkanman.controller;

import com.jkm.jimkanman.dto.LocationDto;
import com.jkm.jimkanman.global.error.ErrorResponse;
import com.jkm.jimkanman.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeliveryMessageController {
    private final SimpMessagingTemplate messagingTemplate; // WebSocket으로 전송
    private final DeliveryService deliveryService; // DB에 위치 저장

    /** 배송 위치 수정 */
    @MessageMapping("/delivery/location") // 클라이언트가 전송할 경로
    public void updateLocation(LocationDto request) {
        // DB에 위치 저장
        deliveryService.updateDeliveryLocation(request);

        // WebSocket으로 클라이언트에 위치 전송
        messagingTemplate.convertAndSend(
                "/topic/delivery/" + request.getDeliveryId(), request
        );
    }

    /** Exception 핸들러 */
    @MessageExceptionHandler
    public void handleException(Exception e) {
        messagingTemplate.convertAndSend(
                "/topic/errors",
                ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }
}
