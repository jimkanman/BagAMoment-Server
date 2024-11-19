package com.jkm.jimkanman.global.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
    private boolean isSuccess;
    private int code;
    private String message;
    private String data;

    private ErrorResponse(){}

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .data("")
                .build();
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(httpStatus.value())
                .message(message)
                .data("")
                .build();
    }
}