package com.example.norush.dto;

public record SignUpRequest(
        String email,
        String password,
        String confirmPassword,
        String username,
        Integer age,
        String gender // 성별 (MALE, FEMALE)
) {

}