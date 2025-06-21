package com.example.norush.domain;

import com.example.norush.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Calendar")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private Integer year;
    private Integer month;
    private Integer day;
    private String memo;

    public void update(Integer year, Integer month, Integer day, String memo) {
        if (year != null) {
            this.year = year;
        }
        if (month != null) {
            this.month = month;
        }
        if (day != null) {
            this.day = day;
        }
        if (memo != null) {
            this.memo = memo;
        }
    }
}