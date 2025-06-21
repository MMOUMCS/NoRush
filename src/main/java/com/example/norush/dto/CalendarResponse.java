package com.example.norush.dto;

import com.example.norush.domain.Calendar;
import java.time.LocalDateTime;

public record CalendarResponse(
    String id,
    String userId,  // email?
    Integer year,
    Integer month,
    Integer day, 
    String memo,
    LocalDateTime createdAt
) {
    public static CalendarResponse from(Calendar calendar) {
        return new CalendarResponse(
            calendar.getId(),
            calendar.getUserId(),
            calendar.getYear(),
            calendar.getMonth(),
            calendar.getDay(),
            calendar.getMemo(),
            calendar.getCreatedAt()
        );
    }
}