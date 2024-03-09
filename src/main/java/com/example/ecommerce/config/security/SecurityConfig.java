package com.example.ecommerce.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //1. csrf filter disable
        http.csrf(csrfCustomizer -> csrfCustomizer.disable());

        //2. session disable
        http.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //3. form login disable
        http.formLogin(formLoginCustomizer -> formLoginCustomizer.disable());

        //4. http Basic disable
        http.httpBasic(httpBasicCustomizer -> httpBasicCustomizer.disable());

        //5. 권한에 따라 url mapping
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer.requestMatchers("/api/v1/user/**")
                                                                                                     .hasAnyAuthority("user", "manager", "admin")
                                                                                                     .requestMatchers("/api/v1/manager/**")
                                                                                                     .hasAnyAuthority("manager", "admin")
                                                                                                     .requestMatchers("/api/v1/admin/**")
                                                                                                     .hasAuthority("admin")
                                                                                                     .anyRequest()
                                                                                                     .permitAll());

        //6. corsFilter 설정 추가
        // 만약 Controller에 @CrossOrigin을 붙여주면 -> 인증이 없어도 되는 요청만 cors 정책에 안걸리게 되므로 -> 로그인을 해야 할 수 있는 요청들은 어차피 cors 정책에 걸리게 됨
        http.addFilter(corsFilter); // 모든 요청이 와도 -> 이 corssFilter를 거치게 되고 -> 이를 통해 cors 정책에 걸리지 않고, 요청이 서버까지 들어온다

        return http.build();
    }
}
