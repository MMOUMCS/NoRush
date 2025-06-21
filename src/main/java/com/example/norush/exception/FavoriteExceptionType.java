package com.example.norush.exception;

import com.example.norush.common.exception.ExceptionType;
import com.example.norush.common.exception.Status;

public enum FavoriteExceptionType implements ExceptionType {

    FAVORITE_NOT_FOUND(Status.NOT_FOUND, 2001, "요청한 즐겨찾기를 찾을 수 없습니다."),
    FAVORITE_ALREADY_EXISTS(Status.CONFLICT, 2002, "동일한 이름의 즐겨찾기가 이미 존재합니다."),
    INVALID_FAVORITE_DATA(Status.BAD_REQUEST, 2003, "즐겨찾기 데이터 형식이 유효하지 않습니다.")
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    FavoriteExceptionType(Status status, int exceptionCode, String message) {
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