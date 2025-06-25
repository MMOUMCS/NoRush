package com.example.norush.dto;

import com.example.norush.domain.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberResponse(
        String id,
        String username,
        String email,
        Boolean isValid,
        String profilePicture,
        int age,
        String gender,
        String role,
        List<String> favorList

) {
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .isValid(member.getIsValid())
                .profilePicture(member.getProfilePicture())
                .age(member.getAge())
                .gender(member.getGender())
                .role(member.getRole())
                .favorList(member.getFavorList())
                .build();
    }
}


