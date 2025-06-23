package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;
import com.example.norush.common.exception.ExceptionType;

public class MemoException extends BaseException {

    private final MemoExceptionType exceptionType;

    public MemoException(MemoExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    @Override
    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
