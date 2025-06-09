package com.example.norush.service;

import static com.example.norush.exception.AuthExceptionType.INVALID_EMAIL;
import static com.example.norush.exception.AuthExceptionType.INVALID_PASSWORD;
import static com.example.norush.exception.AuthExceptionType.INVALID_TOKEN;
import static com.example.norush.exception.MemberExceptionType.USER_NOT_FOUND;

import com.example.norush.config.BCryptPasswordEncoder;
import com.example.norush.domain.Member;
import com.example.norush.domain.RefreshToken;
import com.example.norush.dto.*;
import com.example.norush.exception.AuthException;
import com.example.norush.exception.MemberException;
import com.example.norush.exception.MemberExceptionType;
import com.example.norush.repository.MemberRepository;
import com.example.norush.repository.RefreshTokenRepository;
import com.example.norush.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        validateDuplicateEmail(request.email());
        Member newMember = Member.builder()
                .email(request.email())
                .password(encoded(request.password()))
                .build();
        Member savedMember = memberRepository.save(newMember);
        String refreshToken = jwtUtil.createRefreshToken(savedMember);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberId(savedMember.getId())
                        .token(refreshToken)
                        .build()
        );
        return TokenResponse.builder()
                .accessToken(jwtUtil.createAccessToken(savedMember))
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public TokenResponse signIn(SignInRequest request) {
        Member member = findMemberByEmail(request.email());
        validateSignInRequest(member.getId(), request.password());
        return TokenResponse.builder()
                .accessToken(jwtUtil.createAccessToken(member))
                .refreshToken(getRefreshToken(member.getId()))
                .build();
    }

//    @Transactional
//    public void changePassword(String memberId, PasswordRequest request) {
//        Member member = findMemberById(memberId);
//        if (!isPasswordValid(memberId, request.oldPassword())) {
//            throw new AuthException(INVALID_PASSWORD);
//        }
//        member.changePassword(encoded(request.newPassword()));
//        memberRepository.save(member);
//    }

    @Transactional
    public TokenResponse refresh(RefreshTokenRequest request) {
        Member member = findMemberById(jwtUtil.getMemberIdFromToken(request.refreshToken()));
        refreshTokenRepository.deleteByMemberId(member.getId());
        String refreshToken = jwtUtil.createRefreshToken(member);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberId(member.getId())
                        .token(refreshToken)
                        .build()
        );
        return TokenResponse.builder()
                .accessToken(jwtUtil.createAccessToken(member))
                .refreshToken(refreshToken)
                .build();
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new AuthException(INVALID_EMAIL);
        }
    }

    private String encoded(String password) {
        return BCryptPasswordEncoder.encode(password);
    }

    private void validateSignInRequest(String memberId, String password) {
        validatePassword(memberId, password);
        validateRefreshToken(memberId);
    }

    private void validateRefreshToken(String memberId) {
        String refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN))
                .getToken();
        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            throw new AuthException(INVALID_TOKEN);
        }
    }

    private void validatePassword(String memberId, String password) {
        if (!isPasswordValid(memberId, password)) {
            throw new AuthException(INVALID_PASSWORD);
        }
    }

    private boolean isPasswordValid(String memberId, String password) {
        Member member = findMemberById(memberId);
        return BCryptPasswordEncoder.matches(password, member.getPassword());
    }

    public Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    }

    private String getRefreshToken(String memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN))
                .getToken();
    }


}
