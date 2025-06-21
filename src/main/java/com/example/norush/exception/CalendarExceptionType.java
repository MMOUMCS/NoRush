package com.example.norush.exception;

import com.example.norush.common.exception.ExceptionType;
import com.example.norush.common.exception.Status;

public enum CalendarExceptionType implements ExceptionType {

    CALENDAR_NOT_FOUND(Status.NOT_FOUND, 3001, "요청한 캘린더 일정을 찾을 수 없습니다."),
    CALENDAR_ALREADY_EXISTS(Status.CONFLICT, 3002, "해당 날짜에 이미 일정이 존재합니다."),
    INVALID_CALENDAR_DATA(Status.BAD_REQUEST, 3003, "캘린더 데이터 형식이 유효하지 않습니다.")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    CalendarExceptionType(Status status, int exceptionCode, String message) {
        this.status = status;
        this.exceptionCode = exceptionCode;
        this.message = message;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public int exceptionCode() {
        return exceptionCode;
    }

    @Override
    public String message() {
        return message;
    }
}