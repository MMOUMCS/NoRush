package com.example.norush.util;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로깅 활성화
public class MemoUtil {

    private MemoUtil() {
    }

    public static String generateMemoId() {
        String uuidWithoutHyphens = UUID.randomUUID().toString().replace("-", "");

        String generatedId = uuidWithoutHyphens.substring(0, Math.min(uuidWithoutHyphens.length(), 20));

        log.debug("Generated Memo ID: {}", generatedId);
        return generatedId;
    }

}