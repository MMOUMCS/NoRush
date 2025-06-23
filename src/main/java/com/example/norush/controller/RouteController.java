package com.example.norush.controller;

import com.example.norush.dto.RouteSearchRequest;
import com.example.norush.dto.RouteSearchResponse;
import com.example.norush.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/search")
    public ResponseEntity<RouteSearchResponse> searchRoutes(
            @RequestBody RouteSearchRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();

        RouteSearchResponse response = routeService.searchRoutes(memberId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
