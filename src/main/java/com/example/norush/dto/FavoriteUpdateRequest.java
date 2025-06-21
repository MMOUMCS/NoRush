package com.example.norush.dto;

public record FavoriteUpdateRequest (
    String name,
    String type,
    String targetId,
    String memo

){
    
}
