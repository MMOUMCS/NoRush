package com.example.norush.controller;

import com.example.norush.dto.CalendarAddRequest;
import com.example.norush.dto.CalendarResponse;
import com.example.norush.dto.CalendarUpdateRequest;
import com.example.norush.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping
    public ResponseEntity<CalendarResponse> addCalendarEntry(
            @RequestBody CalendarAddRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        CalendarResponse response = calendarService.addCalendarEntry(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CalendarResponse>> getCalendarEntries(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        List<CalendarResponse> response = calendarService.getCalendarEntries(memberId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> getCalendarEntryById(
            @PathVariable String calendarId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        CalendarResponse response = calendarService.getCalendarEntryById(calendarId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> updateCalendarEntry(
            @PathVariable String calendarId,
            @RequestBody CalendarUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        CalendarResponse response = calendarService.updateCalendarEntry(calendarId, memberId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{calendarId}")
    public ResponseEntity<Void> deleteCalendarEntry(
            @PathVariable String calendarId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        calendarService.deleteCalendarEntry(calendarId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}