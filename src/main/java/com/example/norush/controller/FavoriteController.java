package com.example.norush.controller;

import com.example.norush.dto.FavoriteAddRequest;
import com.example.norush.dto.FavoriteResponse;
import com.example.norush.dto.FavoriteUpdateRequest;
import com.example.norush.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor

public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(
            @RequestBody FavoriteAddRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();

        FavoriteResponse response = favoriteService.addFavorite(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        List<FavoriteResponse> response = favoriteService.getFavorites(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{favoriteId}")
    public ResponseEntity<FavoriteResponse> getFavoriteById(
            @PathVariable String favoriteId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        FavoriteResponse response = favoriteService.getFavoriteById(favoriteId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{favoriteId}")
    public ResponseEntity<FavoriteResponse> updateFavorite(
            @PathVariable String favoriteId,
            @RequestBody FavoriteUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        FavoriteResponse response = favoriteService.updateFavorite(favoriteId, memberId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
            @PathVariable String favoriteId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        favoriteService.deleteFavorite(favoriteId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}