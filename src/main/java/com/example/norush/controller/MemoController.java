package com.example.norush.controller;

import com.example.norush.dto.MemoAddRequest;
import com.example.norush.dto.MemoResponse;
import com.example.norush.dto.MemoUpdateRequest;
import com.example.norush.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoResponse> addMemo(
            @RequestBody MemoAddRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        MemoResponse response = memoService.addMemo(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MemoResponse>> getMemos(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        List<MemoResponse> response = memoService.getMemos(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<MemoResponse> getMemoById(
            @PathVariable String memoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        MemoResponse response = memoService.getMemoById(memoId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{memoId}")
    public ResponseEntity<MemoResponse> updateMemo(
            @PathVariable String memoId,
            @RequestBody MemoUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        MemoResponse response = memoService.updateMemo(memoId, memberId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(
            @PathVariable String memoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String memberId = userDetails.getUsername();
        memoService.deleteMemo(memoId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}