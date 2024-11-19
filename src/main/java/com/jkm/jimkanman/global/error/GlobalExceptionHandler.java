package com.jkm.jimkanman.global.error;

import com.jkm.jimkanman.global.error.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valid 처리 중 발생한 예외 핸들링
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        System.out.println("ExceptionHandler: handling MethodArgumentNotValidException - " + e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                });
        final ErrorResponse errorBaseResponse = ErrorResponse.of(
                ErrorCode.BAD_REQUEST,
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /** ModelAttribute의 Binding 익셉션 핸들링 */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        System.out.println("ExceptionHandler: handling BindException - " + e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /** RequestParam annotation의 binding error를 handling합니다. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        System.out.println("ExceptionHandler: handling MethodArgumentTypeMismatchExcepton - " + e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "Request Parameter 형식이 잘못되었습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /** 지원하지 않는 HTTP method로 요청 시 던져진 익셉션 핸들링 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        System.out.println("ExceptionHandler : HttpRequestMethodNotSupportedException - " + e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBaseResponse);
    }

    /** BusinessException을 handling합니다. */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        System.out.println("ExceptionHandler : BusinessException " + e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorBaseResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorBaseResponse);
    }


    /** 나머지 예외 핸드링 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(MethodArgumentNotValidException e, WebRequest request) {
        System.out.println("ExceptionHandler: handling exception - " + e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBaseResponse);
    }
}
