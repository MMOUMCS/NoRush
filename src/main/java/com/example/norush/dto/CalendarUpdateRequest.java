package com.example.norush.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;


public record CalendarUpdateRequest(
    @Min(value = 2025, message = "연도는 2025년 이후여야 합니다.")
    Integer year,

    @Min(value = 1, message = "월은 1월 이상이어야 합니다.")
    @Max(value = 12, message = "월은 12월 이하여야 합니다.")
    Integer month,

    @Min(value = 1, message = "일은 1일 이상이어야 합니다.")
    @Max(value = 31, message = "일은 31일 이하여야 합니다.")
    Integer day,

    @Size(max = 100, message = "메모는 최대 100자까지 가능합니다.")
    String memo
) {}