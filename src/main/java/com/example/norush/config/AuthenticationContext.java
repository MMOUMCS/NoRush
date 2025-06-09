package com.example.norush.config;

import com.example.norush.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

import static com.example.norush.exception.AuthExceptionType.UNAUTHORIZED;

@RequestScope
@Component
@Slf4j
public class AuthenticationContext {

    private String memberId;

    public void setAuthentication(String memberId) {
        this.memberId = memberId;
    }

    public String getPrincipal() {
        if (Objects.isNull(memberId)) {
            throw new AuthException(UNAUTHORIZED);
        }
        return memberId;
    }
}
