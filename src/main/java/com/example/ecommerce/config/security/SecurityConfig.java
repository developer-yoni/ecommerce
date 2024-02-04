package com.example.ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// 스프링 시큐리티 필터가 -> 스프링 기본 필터체인에 등록이 된다
// 그리고 그 시큐리티 필터란, 이 SecurityConfig의 설정 정보에 따라 적절하게 생성된다
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf 필터는 disable
        http.csrf(AbstractHttpConfigurer::disable);

        // /user/** , /manager/** , /admin/** 은 인증과 동시에 권한을 요구하도록 설정 + 그외는 인증 없어도 됨
        http.authorizeHttpRequests(authorizeRequest -> authorizeRequest.requestMatchers("/user/**")
                                                                       .authenticated()
                                                                       .requestMatchers("/manager/**")
                                                                       .hasAnyAuthority("admin", "manager")
                                                                       .requestMatchers("/admin/**")
                                                                       .hasAuthority("admin")
                                                                       .anyRequest().permitAll());

        // [새로움] 이 설정을 추가함으로써 -> /user , /manager, /admin 으로 인증 없이 이동하려고 하면, /login 페이지로 리다이렉트 된다
        http.formLogin(formLogin -> formLogin.loginPage("/login"));

        return http.build();
    }
}
