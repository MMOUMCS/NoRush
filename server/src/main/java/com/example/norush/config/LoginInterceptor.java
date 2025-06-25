//package com.example.norush.config;
//
//import com.example.norush.exception.AuthException;
//import com.example.norush.util.JwtUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import static com.example.norush.exception.AuthExceptionType.UNAUTHORIZED;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class LoginInterceptor implements HandlerInterceptor {
//
//    private final JwtUtil jwtUtil;
//    private final AuthenticationContext authenticationContext;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String token = AuthenticationExtractor.extract(request)
//                .orElseThrow(() -> new AuthException(UNAUTHORIZED));
//        String memberId = jwtUtil.getMemberIdFromToken(token);
//        log.info("userId: {}", memberId);
//        authenticationContext.setAuthentication(memberId);
//        return true;
//    }
//}
