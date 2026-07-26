package com.gdgoc.website.global.exception;

import com.gdgoc.website.global.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // BaseException (커스텀 에러)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        String errorMessage = e.getMessage();

        if (!errorCode.getStatus().is5xxServerError()) {
            log.error(errorCode + " - " + errorMessage);
        }

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode, errorMessage));
    }

    // RequestBody 검증 실패한 경우 (필수 입력 항목 누락 / 형식 오류)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message = "필수 입력 항목이 누락되었거나 잘못된 형식입니다.";

        // 입력 형식 오류 시
        if (fieldError != null) {
            String code = fieldError.getCode();

            log.warn("MethodArgumentNotValidException - field: {}, value: {}, constraint: {}",
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getCode());

            // 메시지 변경
            if ("Pattern".equals(code) || "Email".equals(code) || "Size".equals(code)
                    || "Min".equals(code) || "Max".equals(code)) {
                message = fieldError.getDefaultMessage();
            }
        }

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, message));
    }

    // 요청 본문 파싱에 실패한 경우 (Enum 값이 잘못된 경우 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getMostSpecificCause();

        if (cause instanceof InvalidFormatException ex) { // 타입 오류, Enum 오류
            log.warn("HttpMessageNotReadableException - field: {}, value: {}",
                    ex.getPathReference(),
                    ex.getValue());
        } else {
            log.warn("HttpMessageNotReadableException - {}", cause.getMessage());
        }

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, "잘못된 요청 본문입니다."));
    }

}
