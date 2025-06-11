package com.example.norush.exception;

import com.example.norush.common.exception.BaseException;
import com.example.norush.common.exception.ExceptionType;

public class AuthException extends BaseException {
    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
