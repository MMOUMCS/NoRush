package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;

public class MemberException extends BaseException {

    private final MemberExceptionType exceptionType;

    public MemberException(MemberExceptionType exceptionType) {
        super(exceptionType);
        this.exceptionType = exceptionType;
    }

    public MemberExceptionType getExceptionType() {
        return exceptionType;
    }
}
