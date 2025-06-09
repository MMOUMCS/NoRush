package com.example.norush.controller;

import com.example.norush.domain.Member;
import com.example.norush.dto.*;
import com.example.norush.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<TokenResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(request));
    }

    @PostMapping("/signIn")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.refresh(request));
    }

//    @PostMapping("/password")
//    public ResponseEntity<Void> changePassword(@AuthUser Long userId, @RequestBody PasswordRequest request) {
//        memberService.changePassword(userId, request);
//        return ResponseEntity.ok().build();
}
