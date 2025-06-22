package com.example.norush.exception;

import com.example.norush.common.exception.ExceptionType;
import com.example.norush.common.exception.Status;

public enum RouteExceptionType implements ExceptionType {

    NO_ROUTES_FOUND(Status.NOT_FOUND, 5001, "요청하신 경로에 대한 정보를 찾을 수 없습니다."), // HTTP 404
    INVALID_LOCATION(Status.BAD_REQUEST, 5002, "출발지 또는 도착지 정보가 유효하지 않습니다."), // HTTP 400
    EXTERNAL_TRAFFIC_API_ERROR(Status.SERVICE_UNAVAILABLE, 5003, "교통 정보 API 연동 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // HTTP 503
    AI_PREDICTION_ERROR(Status.SERVICE_UNAVAILABLE, 5004, "혼잡도 예측 시스템 연동 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // HTTP 503
    ROUTE_OPTIMIZATION_FAILED(Status.INTERNAL_SERVER_ERROR, 5005, "최적 경로 계산 중 오류가 발생했습니다.") // HTTP 500
    ;

    private final Status status;
    private final int exceptionCode;
    private final String message;

    RouteExceptionType(Status status, int exceptionCode, String message) {
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