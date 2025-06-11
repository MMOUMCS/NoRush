package com.example.norush.dto;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        String refreshToken
) {

}
