package com.example.norush.exception;

import com.example.norush.common.exception.ExceptionType;
import com.example.norush.common.exception.Status;

public enum MemberExceptionType implements ExceptionType {
    USER_NOT_FOUND(Status.UNAUTHORIZED, 1001, "존재하지 않는 사용자입니다")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    MemberExceptionType(Status status, int exceptionCode, String message) {
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
