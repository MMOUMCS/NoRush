package com.example.norush.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemoAddRequest(
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 50, message = "제목은 최대 50자까지 가능합니다.")
    String title,

    @Size(max = 100, message = "100자를 초과했습니다.")
    String content
) {}