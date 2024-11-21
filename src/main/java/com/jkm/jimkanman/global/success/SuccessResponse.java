package com.jkm.jimkanman.global.success;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class SuccessResponse<T> {
    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    private Integer code;
    private String message;
    private T data;

    private SuccessResponse(){}

    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(SuccessCode.OK, data));
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(SuccessCode.CREATED, data));
    }


    public static <T> SuccessResponse<T> of(SuccessCode successCode, T data) {
        return SuccessResponse.<T>builder()
                .isSuccess(true)
                .code(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }
}
