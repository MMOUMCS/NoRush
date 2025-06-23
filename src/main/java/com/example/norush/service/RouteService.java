package com.example.norush.service;

import static com.example.norush.exception.MemberExceptionType.USER_NOT_FOUND;
import static com.example.norush.exception.RouteExceptionType.*;

import com.example.norush.client.AIPredictionClient;
import com.example.norush.domain.Member;
import com.example.norush.dto.AIPredictionRequest;
import com.example.norush.dto.AIPredictionResponse;
import com.example.norush.dto.RouteSearchRequest;
import com.example.norush.dto.RouteSearchRequest.TimeCriteria;
import com.example.norush.dto.RouteSearchResponse;
import com.example.norush.dto.RouteSearchResponse.RecommendedRoute;
import com.example.norush.dto.RouteSegmentResponse;
import com.example.norush.dto.RouteSegmentResponse.CongestionInfo;
import com.example.norush.dto.RouteSegmentResponse.CompartmentCongestion;
import com.example.norush.exception.MemberException;
import com.example.norush.exception.RouteException;
import com.example.norush.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final MemberRepository memberRepository;
    private final AIPredictionClient aiPredictionClient;
    // 필요한 경우 외부 교통 API 클라이언트 주입 (예: KakaoMapClient, SeoulTrafficApiClient)
    // private final KakaoMapClient kakaoMapClient;
    // private final SeoulTrafficApiClient seoulTrafficApiClient;

    @Transactional(readOnly = true)
    public RouteSearchResponse searchRoutes(String memberId, RouteSearchRequest request) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        // 1. 출발지/도착지 유효성 검사 및 좌표 변환 (Kakao Map API 등 활용)
        // 실제 구현에서는 주소를 좌표로 변환하거나, 역/정류장 이름을 ID로 변환하는 로직이 필요합니다.
        // DSD에 Kakao Map API 연동 명시.
        validateLocations(request.departureLocation(), request.arrivalLocation());

        // 2. 외부 교통 API를 통해 실시간/기본 교통 정보 수집 (서울시 교통정보 OpenAPI 등 활용)
        // DSD에 서울시 교통정보 OpenAPI 명시.
        // 예: 버스 노선, 지하철 노선, 정류장/역 정보, 기본 운행 시간표
        List<TrafficData> rawTrafficData = fetchRealtimeTrafficData(request.departureLocation(), request.arrivalLocation());
        if (rawTrafficData.isEmpty()) {
            throw new RouteException(NO_ROUTES_FOUND); // 경로를 찾을 수 없음
        }

        List<RecommendedRoute> recommendedRoutes = new ArrayList<>();


        List<RouteSegmentResponse> segments1 = new ArrayList<>();
        AtomicInteger segmentIdCounter1 = new AtomicInteger(1);

        // 버스 구간
        segments1.add(createSampleBusSegment(
                "출발 정류장", "환승 정류장",
                request.dateTime,
                request.dateTime.plusMinutes(15),
                "147", "200100103", "12345", "3" // 예시: 버스 노선ID, 정류장ID, 칸번호 (지하철 아님)
        ).block());

        // 지하철 구간
        segments1.add(createSampleSubwaySegment(
                "환승 역", "도착 역",
                request.dateTime.plusMinutes(20),
                request.dateTime.plusMinutes(40),
                "1호선", "158", "청량리", "5" // 예시: 지하철 노선ID, 역번호, 역명, 칸번호
        ).block()); // Mono 결과 대기


        recommendedRoutes.add(new RecommendedRoute(
                "route_" + UUID.randomUUID().toString().substring(0, 8),
                "버스 1회, 지하철 1회 환승",
                45, // 총 소요 시간 (분)
                2,  // 총 환승 횟수
                calculateAverageCongestion(segments1), // 구간별 혼잡도 평균 계산
                segments1,
                "오전 8시 00분 - 8시 30분",
                "보통"
        ));

        List<RouteSegmentResponse> segments2 = new ArrayList<>();
        AtomicInteger segmentIdCounter2 = new AtomicInteger(1);

        segments2.add(createSampleSubwaySegment(
                "출발 역", "도착 역",
                request.dateTime,
                request.dateTime.plusMinutes(30),
                "2호선", "203", "강남", "7" // 예시: 지하철 노선ID, 역번호, 역명, 칸번호
        ).block());

        recommendedRoutes.add(new RecommendedRoute(
                "route_" + UUID.randomUUID().toString().substring(0, 8),
                "지하철 직통",
                30,
                0,
                calculateAverageCongestion(segments2),
                segments2,
                "오전 9시 30분 - 10시 00분",
                "여유"
        ));

        sortRoutesByPreference(recommendedRoutes, request.timeCriteria());


        return new RouteSearchResponse(recommendedRoutes);
    }

    private void validateMemberExistence(String memberId) {
        if (!memberRepository.findById(memberId).isPresent()) {
            throw new MemberException(USER_NOT_FOUND);
        }
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    }

    private void validateLocations(String departureLocation, String arrivalLocation) {
        if (departureLocation.isEmpty() || arrivalLocation.isEmpty()) {
            throw new RouteException(INVALID_LOCATION);
        }
        // 실제로는 카카오맵 API 등을 호출하여 유효한 위치인지, 경로 탐색이 가능한지 확인
        // DSD에 Kakao Map API 연동 명시.
    }

    private List<TrafficData> fetchRealtimeTrafficData(String departureLocation, String arrivalLocation) {

        log.info("Fetching realtime traffic data for {} to {}", departureLocation, arrivalLocation);

        return List.of(new TrafficData("Sample Bus Data"), new TrafficData("Sample Subway Data"));
    }

    private Mono<RouteSegmentResponse> createSampleBusSegment(
            String startPointName, String endPointName,
            LocalDateTime departureTime, LocalDateTime arrivalTime,
            String busRouteId, String busStopId, String compartmentInfo
    ) {
        AIPredictionRequest aiRequest = new AIPredictionRequest(
                busRouteId,
                busStopId,
                compartmentInfo,
                departureTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        log.info("Requesting AI prediction for bus: {}", aiRequest);

        return aiPredictionClient.predictBusCongestion(aiRequest)
                .map(aiResponse -> {
                    CongestionInfo congestionInfo = new CongestionInfo(
                            mapCongestionValueToLevel(aiResponse.predictedCongestion()),
                            aiResponse.predictedCongestion()
                    );
                    return new RouteSegmentResponse(
                            "BUS",
                            startPointName + "에서 " + busRouteId + "번 버스 탑승",
                            (int) (java.time.Duration.between(departureTime, arrivalTime).toMinutes()),
                            startPointName,
                            endPointName,
                            departureTime,
                            arrivalTime,
                            congestionInfo,
                            null
                    );
                })
                .doOnError(e -> log.error("Error creating bus segment with AI prediction: {}", e.getMessage(), e));
    }

    private Mono<RouteSegmentResponse> createSampleSubwaySegment(
            String startPointName, String endPointName,
            LocalDateTime departureTime, LocalDateTime arrivalTime,
            String subwayLineId, String subwayStationId, String subwayStationName, String compartmentNumber
    ) {
        AIPredictionRequest aiRequest = new AIPredictionRequest(
                subwayLineId,
                subwayStationId,
                compartmentNumber,
                departureTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        log.info("Requesting AI prediction for subway: {}", aiRequest);

        return aiPredictionClient.predictSubwayCongestion(aiRequest)
                .map(aiResponse -> {
                    CongestionInfo congestionInfo = new CongestionInfo(
                            mapCongestionValueToLevel(aiResponse.predictedCongestion()),
                            aiResponse.predictedCongestion()
                    );
                    List<CompartmentCongestion> compartmentCongestions = new ArrayList<>();
                    compartmentCongestions.add(new CompartmentCongestion(
                            aiResponse.compartment(),
                            Double.parseDouble(aiResponse.predictedCongestionCompartment())
                    ));

                    return new RouteSegmentResponse(
                            "SUBWAY",
                            startPointName + "에서 " + subwayLineId + " 탑승 (" + subwayStationName + "역)",
                            (int) (java.time.Duration.between(departureTime, arrivalTime).toMinutes()),
                            startPointName,
                            endPointName,
                            departureTime,
                            arrivalTime,
                            congestionInfo,
                            subwayLineId,
                            subwayStationName, // 지하철 역명
                            aiResponse.subwayTrainNumber(),
                            compartmentCongestions
                    );
                })
                .doOnError(e -> log.error("Error creating subway segment with AI prediction: {}", e.getMessage(), e));
    }

    private String mapCongestionValueToLevel(double congestionValue) {

        if (congestionValue <= 30) {
            return "여유";
        } else if (congestionValue <= 70) {
            return "보통";
        } else {
            return "혼잡";
        }
    }

    private void sortRoutesByPreference(List<RecommendedRoute> routes, TimeCriteria timeCriteria) {
        if (timeCriteria == TimeCriteria.DEPARTURE) {
            routes.sort((r1, r2) -> Integer.compare(r1.totalTravelTimeMinutes(), r2.totalTravelTimeMinutes()));
        } else if (timeCriteria == TimeCriteria.ARRIVAL) {.
            routes.sort((r1, r2) -> Double.compare(r1.averageCongestion(), r2.averageCongestion()));
        }
    }

    private double calculateAverageCongestion(List<RouteSegmentResponse> segments) {
        if (segments == null || segments.isEmpty()) {
            return 0.0;
        }
        double totalCongestion = 0;
        int count = 0;
        for (RouteSegmentResponse segment : segments) {
            if (segment.congestionInfo() != null) {
                totalCongestion += segment.congestionInfo().predictedCongestion();
                count++;
            }
        }
        return count > 0 ? totalCongestion / count : 0.0;
    }

    private static class TrafficData {
        String data;
        public TrafficData(String data) { this.data = data; }
    }
}