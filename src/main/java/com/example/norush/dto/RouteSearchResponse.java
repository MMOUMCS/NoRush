package com.example.norush.dto;

import java.util.List;

public record RouteSearchResponse(
    List<RecommendedRoute> recommendedRoutes
) {
    public record RecommendedRoute(
        String routeId,
        String summary, 
        int totalTravelTimeMinutes,
        int totalTransferCount,
        double averageCongestion,
        List<RouteSegmentResponse> segments, // 경로를 구성하는 각 구간 정보
        String optimalTimeWindow,    // 추천 시간대
        String optimalCongestionLevel // 해당 시간대 혼잡도
    ) {}
}