package com.example.norush.dto;

import com.example.norush.domain.Memo;
import java.time.LocalDateTime;

public record MemoResponse(
    String id,
    String userId,
    String title, 
    String content,
    LocalDateTime timestamp
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
            memo.getId(),
            memo.getUserId(),
            memo.getTitle(),
            memo.getContent(),
            memo.getTimestamp()
        );
    }
}