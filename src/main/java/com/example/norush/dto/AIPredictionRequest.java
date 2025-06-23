package com.example.norush.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AIPredictionRequest(
    @JsonProperty("route_id")
    @NotBlank(message = "노선 ID는 필수입니다.")
    String routeId,

    @JsonProperty("stop_id")
    @NotBlank(message = "정류장/역 ID는 필수입니다.")
    String stopId,

    @JsonProperty("compartment")
    @NotBlank(message = "칸 번호는 필수입니다.")
    @Pattern(regexp = "^([1-9]|10)$", message = "칸 번호는 1에서 10 사이여야 합니다.") // 대구의 경우 6이기에 6까지만? 어떻게 쓰는 지 추후 확인
    String compartment,

    @JsonProperty("datetime")
    @NotBlank(message = "날짜 및 시간은 필수입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "날짜 및 시간 형식은 YYYY-MM-DDTHH:mm:ss 여야 합니다.")
    String datetime
) {}
