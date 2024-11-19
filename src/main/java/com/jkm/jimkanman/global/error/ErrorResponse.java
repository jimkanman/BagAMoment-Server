package com.jkm.jimkanman.global.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ErrorResponse<T> {
    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    private Integer code;
    private String message;
    private T data;

    private ErrorResponse(){}

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ErrorResponse of(ErrorCode errorCode, T data) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(httpStatus.value())
                .message(message)
                .data(null)
                .build();
    }
}