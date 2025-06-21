package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;
import com.example.norush.common.exception.ExceptionType;

public class FavoriteException extends BaseException {

    private final FavoriteExceptionType exceptionType;

    public FavoriteException(FavoriteExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}