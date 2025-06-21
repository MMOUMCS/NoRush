package com.example.norush.dto;

public record FavoriteAddRequest(
    String name,
    String type, // 유형
    String targetId,   // 버스 노선이나 지하철 역 ID등 경로
    String memo

){

}