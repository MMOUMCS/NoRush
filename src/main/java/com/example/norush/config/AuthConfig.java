//package com.example.norush.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class AuthConfig implements WebMvcConfigurer {
//
//    private final AuthArgumentResolver authArgumentResolver;
//    private final LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor());
//    }
//
//    private HandlerInterceptor loginInterceptor() {
//        return new PathMatcherInterceptor(loginInterceptor)
//                .excludePathPattern("/**", OPTIONS)
//                .addPathPattern("/members/**", GET, POST)
//                .addPathPattern("/auth/password", POST);
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(authArgumentResolver);
//    }
//
//
//}
