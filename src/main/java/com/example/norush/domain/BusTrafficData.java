package com.example.norush.domain;

import com.example.norush.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "bus_traffic_data")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusTrafficData extends BaseEntity {

    @Id
    private String id;

    @Field("vehicle_id")
    private String vehicleId;

    @Field("route_id")
    private String routeId;

    @Field("route_name") // 버스 이름으로 변경
    private String routeName;

    @Field("current_stop_id")
    private String currentStopId;

    @Field("current_stop_name") // 추가 필요  현재 정류장 id, 이름
    private String currentStopName;

    @Field("next_stop_id")
    private String nextStopId;

    @Field("next_stop_name") // 추가 필요   다음 정류장 id , 이름
    private String nextStopName;

    @Field("congestion_level")
    private Double congestionLevel; 

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("latitude")
    private Double latitude;

    @Field("longitude")
    private Double longitude;

    @Field("day_of_week")
    private String dayOfWeek;

    @Field("first_bus_time")
    private String firstBusTime;

    @Field("last_bus_time")
    private String lastBusTime;

    @Field("dispatch_interval")
    private Integer dispatchInterval;

    public void updateTrafficData(
            String vehicleId, String currentStopId, String currentStopName,
            String nextStopId, String nextStopName, Double congestionLevel,
            Double latitude, Double longitude, String dayOfWeek,
            String firstBusTime, String lastBusTime, Integer dispatchInterval) {
        if (vehicleId != null) this.vehicleId = vehicleId;
        if (currentStopId != null) this.currentStopId = currentStopId;
        if (currentStopName != null) this.currentStopName = currentStopName;
        if (nextStopId != null) this.nextStopId = nextStopId;
        if (nextStopName != null) this.nextStopName = nextStopName;
        if (congestionLevel != null) this.congestionLevel = congestionLevel;
        if (latitude != null) this.latitude = latitude;
        if (longitude != null) this.longitude = longitude;
        if (dayOfWeek != null) this.dayOfWeek = dayOfWeek;
        if (firstBusTime != null) this.firstBusTime = firstBusTime;
        if (lastBusTime != null) this.lastBusTime = lastBusTime;
        if (dispatchInterval != null) this.dispatchInterval = dispatchInterval;
        this.timestamp = LocalDateTime.now();
    }
}
