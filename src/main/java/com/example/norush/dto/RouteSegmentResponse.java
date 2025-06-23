package com.example.norush.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RouteSegmentResponse(
    String segmentType, // 지하철 / 버스
    String instruction, // ~에서 ~ 탑승
    int travelTimeMinutes,
    String startPointName,
    String endPointName,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,

    CongestionInfo congestionInfo,

    String subwayLineName,
    String subwayTrainNumber,
    List<CompartmentCongestion> compartmentCongestions,

    // 버스
    String busRouteNumber,
    String busVehicleId
) {
    public record CongestionInfo(
        String congestionLevel,
        double predictedCongestion
    ) {}

    public record CompartmentCongestion(
        String compartmentNumber,
        double predictedCongestion
    ) {}
}