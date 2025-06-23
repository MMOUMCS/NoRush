package com.example.norush.service;

import com.example.norush.config.CustomOAuth2User;
import com.example.norush.domain.Member;
import com.example.norush.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        Member member;
        if (optionalMember.isEmpty()) {
            Member newMember = Member.builder()
                    .email(email)
                    .password("") // 비번 없음 (또는 고정된 랜덤값)
                    .age(null)
                    .gender(null)
                    .role("USER")
                    .isValid(true)
                    .favorList(new ArrayList<>())
                    .build();
            member = memberRepository.save(newMember);
        } else {
            member = optionalMember.get();
        }

        return new CustomOAuth2User(oauth2User.getAttributes(), member);
    }
}