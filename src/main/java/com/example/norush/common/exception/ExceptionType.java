package com.example.norush.common.exception;

public interface ExceptionType {
    Status status();

    int exceptionCode();

    String message();
}
