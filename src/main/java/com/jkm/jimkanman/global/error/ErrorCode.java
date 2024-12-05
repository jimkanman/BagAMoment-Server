package com.jkm.jimkanman.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    /** 400 Bad Request */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    REQUEST_PARAMETER_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식입니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "잘못된 id 형식입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 형식입니다."),
    STORAGE_ADDRESS_NULL(HttpStatus.BAD_REQUEST, "보관소 주소가 비어있습니다."),
    LUGGAGE_NULL(HttpStatus.BAD_REQUEST, "짐 등록 없이 보관소 예약을 할 수 없습니다."),
    STORAGE_NOT_OPEN(HttpStatus.BAD_REQUEST, "예약시간이 보관소 운영시간과 맞지 않습니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "예약 상태 변경이 올바르지 않습니다."),

    /** 401 Unauthorized */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 없거나 형식이 올바르지 않습니다"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인이 실패하였습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    /** 403 Forbidden */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),
    DELIVERY_NOT_ALLOWED(HttpStatus.FORBIDDEN, "배송 옵션이 비활성화된 보관소입니다."),

    /** 404 Not Found */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    STORAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "보관소를 찾을 수 없습니다"),
    STORAGE_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "보관소 예약을 찾을 수 없습니다"),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 신청을 찾을 수 없습니다"),
    DELIVERY_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 예약을 찾을 수 없습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),

    /** 405 Method Not Allowed */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "보유 밸런스가 부족합니다"),

    /** 409 Conflict */
    ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 id입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),


    /** 500 Internal Server Error */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    /** 503 Service Unavailable */
    EXTERNAL_API_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "외부 API가 응답하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
