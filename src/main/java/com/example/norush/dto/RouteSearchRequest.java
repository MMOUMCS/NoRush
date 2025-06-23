package com.example.norush.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RouteSearchRequest(
    @NotBlank(message = "출발지는 필수 입력 항목입니다.")
    String departureLocation,

    @NotBlank(message = "도착지는 필수 입력 항목입니다.")
    String arrivalLocation,

    @NotNull(message = "출발 또는 도착 시간을 설정해야 합니다.")
    LocalDateTime dateTime,

    @NotNull(message = "시간 기준을 설정해야 합니다 (출발 또는 도착).")
    TimeCriteria timeCriteria
) {

    public enum TimeCriteria {
        DEPARTURE,
        ARRIVAL
    }
}