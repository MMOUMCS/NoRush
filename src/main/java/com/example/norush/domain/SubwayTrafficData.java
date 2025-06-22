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

@Document(collection = "subway_traffic_data")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubwayTrafficData extends BaseEntity {

    @Id
    private String id;

    @Field("train_id")
    private String trainId;

    @Field("line_id")
    private String lineId;

    @Field("line_name") // 3호선 등
    private String lineName;

    @Field("current_station_id")
    private String currentStationId;

    @Field("current_station_name") // 역 id , 이름
    private String currentStationName;

    @Field("next_station_id")
    private String nextStationId;

    @Field("next_station_name") // 다음 역 id , 이름
    private String nextStationName;

    @Field("congestion_level")
    private Double congestionLevel;

    @Field("direction")
    private String direction;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("day_of_week")
    private String dayOfWeek;

    @Field("compartment_number")
    private Integer compartmentNumber;

    public void updateTrafficData(
            String trainId, String lineId, String lineName, String currentStationId, String currentStationName,
            String nextStationId, String nextStationName, Double congestionLevel, String direction,
            String dayOfWeek, Integer compartmentNumber) {
        if (trainId != null) this.trainId = trainId;
        if (lineId != null) this.lineId = lineId;
        if (lineName != null) this.lineName = lineName;
        if (currentStationId != null) this.currentStationId = currentStationId;
        if (currentStationName != null) this.currentStationName = currentStationName;
        if (nextStationId != null) this.nextStationId = nextStationId;
        if (nextStationName != null) this.nextStationName = nextStationName;
        if (congestionLevel != null) this.congestionLevel = congestionLevel;
        if (direction != null) this.direction = direction;
        if (dayOfWeek != null) this.dayOfWeek = dayOfWeek;
        if (compartmentNumber != null) this.compartmentNumber = compartmentNumber;
        this.timestamp = LocalDateTime.now();
    }
}