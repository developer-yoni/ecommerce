package com.example.ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        // 설정이 적용되면, CORS 요청에서 쿠키, HTTP 인증 헤더, TLS 클라이언트 인증과 같은 인증 정보의 자동 전송이 금지됩니다.
        // 즉 쿠키에 저장된 SessionId등이 자동 포함 안됨.
        // 단 JWT를 통한 인증 방식은 쿠키에 저장해놓고 자동 포함시키는 방식이 아니므로, 문제가 없음
        configuration.setAllowCredentials(false);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", configuration);
        return new CorsFilter(source);
    }
}
