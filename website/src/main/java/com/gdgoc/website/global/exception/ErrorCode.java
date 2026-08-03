package com.gdgoc.website.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 지원서 관련
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Application을 찾을 수 없습니다."),
    APPLICATION_INTERNAL_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 Application을 찾을 수 없습니다."),
    APPLICATION_DETAIL_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "지원서 상세 정보를 찾을 수 없습니다."),
    APPLICATION_ALREADY_SUBMITTED(HttpStatus.BAD_REQUEST, "이미 제출된 지원서입니다."),
    INTERVIEW_TIME_SLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Interview Time Slot을 찾을 수 없습니다."),
    INVALID_INTERVIEW_TIME_SLOT(HttpStatus.BAD_REQUEST, "유효하지 않은 Interview Time Slot입니다."),
    INVALID_INTERVIEW_TIME_SLOT_COUNT(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 면접 시간대 개수입니다."),
    INVALID_APPLICATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 Application Type입니다."),
    APPLICATION_PERIOD_CLOSED(HttpStatus.BAD_REQUEST, "지원 기간이 아니거나 마감되었습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
