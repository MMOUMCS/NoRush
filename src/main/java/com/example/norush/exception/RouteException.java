package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;
import com.example.norush.common.exception.ExceptionType;

public class RouteException extends BaseException {

    private final RouteExceptionType exceptionType;

    public RouteException(RouteExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
