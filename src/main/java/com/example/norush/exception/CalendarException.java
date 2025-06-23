package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;
import com.example.norush.common.exception.ExceptionType;

public class CalendarException extends BaseException {

    private final CalendarExceptionType exceptionType;

    public CalendarException(CalendarExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
