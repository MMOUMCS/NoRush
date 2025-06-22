package com.example.norush.client;

import com.example.norush.dto.AIPredictionRequest;
import com.example.norush.dto.AIPredictionResponse;
import com.example.norush.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class AIPredictionClient {

    private final WebClient webClient;

    @Value("${ai.prediction.base-url}")
    private String aiPredictionBaseUrl;

    public AIPredictionClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostConstruct
    public void init() {
        log.info("AIPredictionClient initialized with base URL: {}", aiPredictionBaseUrl);
    }

    public Mono<AIPredictionResponse> predictBusCongestion(AIPredictionRequest request) {
        return webClient.post()
                .uri(aiPredictionBaseUrl + "/predict/bus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()

                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("AI Prediction Client 4xx Error: {} - {}", clientResponse.statusCode(), errorBody);
                            return Mono.error(new ExternalServiceException("AI 예측 시스템 클라이언트 오류: " + errorBody, clientResponse.statusCode().value()));
                        })
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("AI Prediction Client 5xx Error: {} - {}", clientResponse.statusCode(), errorBody);
                            return Mono.error(new ExternalServiceException("AI 예측 시스템 서버 오류: " + errorBody, clientResponse.statusCode().value()));
                        })
                )
                .bodyToMono(AIPredictionResponse.class)
                .doOnError(e -> log.error("Error during AI bus congestion prediction call: {}", e.getMessage(), e));
    }

    public Mono<AIPredictionResponse> predictSubwayCongestion(AIPredictionRequest request) {
        return webClient.post()
                .uri(aiPredictionBaseUrl + "/predict/subway")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("AI Prediction Client 4xx Error: {} - {}", clientResponse.statusCode(), errorBody);
                            return Mono.error(new ExternalServiceException("AI 예측 시스템 클라이언트 오류: " + errorBody, clientResponse.statusCode().value()));
                        })
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("AI Prediction Client 5xx Error: {} - {}", clientResponse.statusCode(), errorBody);
                            return Mono.error(new ExternalServiceException("AI 예측 시스템 서버 오류: " + errorBody, clientResponse.statusCode().value()));
                        })
                )
                .bodyToMono(AIPredictionResponse.class)
                .doOnError(e -> log.error("Error during AI subway congestion prediction call: {}", e.getMessage(), e));
    }

    public Mono<Void> requestModelRetrain() {
        return webClient.post()
                .uri(aiPredictionBaseUrl + "/train")
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("AI Model Retrain Client Error: {} - {}", clientResponse.statusCode(), errorBody);
                            return Mono.error(new ExternalServiceException("AI 모델 재학습 요청 오류: " + errorBody, clientResponse.statusCode().value()));
                        })
                )
                .bodyToMono(Void.class)
                .doOnError(e -> log.error("Error during AI model retrain call: {}", e.getMessage(), e));
    }
}

// 임시로 사용 추후 재구성
