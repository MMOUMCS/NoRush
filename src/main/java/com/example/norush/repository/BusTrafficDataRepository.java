package com.example.norush.repository;

import com.example.norush.domain.BusTrafficData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusTrafficDataRepository extends MongoRepository<BusTrafficData, String> {

    List<BusTrafficData> findByRouteId(String routeId);
    List<BusTrafficData> findByRouteIdAndCurrentStopId(String routeId, String currentStopId);
    List<BusTrafficData> findByRouteIdAndCurrentStopIdAndTimestampBetween(String routeId, String currentStopId, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
    Optional<BusTrafficData> findTopByVehicleIdOrderByTimestampDesc(String vehicleId);
}