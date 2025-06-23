package com.example.norush.exception;

import com.example.norush.common.exception.ExceptionType;
import com.example.norush.common.exception.Status;

public enum MemoExceptionType implements ExceptionType {
    MEMO_NOT_FOUND(Status.NOT_FOUND, 4001, "요청한 메모를 찾을 수 없습니다."),
    MEMO_TITLE_ALREADY_EXISTS(Status.CONFLICT, 4002, "동일한 제목의 메모가 이미 존재합니다."),
    INVALID_MEMO_DATA(Status.BAD_REQUEST, 4003, "메모 데이터 형식이 유효하지 않습니다.")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    MemoExceptionType(Status status, int exceptionCode, String message) {
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
