package com.example.norush.util;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalendarUtil {

    private CalendarUtil() {
    }

    public static String generateCalendarId() {
        String uuidWithoutHyphens = UUID.randomUUID().toString().replace("-", "");

        String generatedId = uuidWithoutHyphens.substring(0, Math.min(uuidWithoutHyphens.length(), 20));

        log.debug("Generated Calendar ID: {}", generatedId); // 디버그 로그 출력
        return generatedId;
    }

}
