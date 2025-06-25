package com.example.norush.common.exception;

public record ExceptionResponse(
        int exceptionCode,
        String message
) {

}