package com.example.norush.domain;

import java.util.Arrays;

public enum Provider {
    NORMAL("normal"),
    GOOGLE("google"),
    KAKAO("kakao");

    private final String providerName;

    Provider(String providerName) {
        this.providerName = providerName;
    }

    public static Provider from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.providerName.equals(name))
                .findFirst()
                .orElseThrow();
    }
}
