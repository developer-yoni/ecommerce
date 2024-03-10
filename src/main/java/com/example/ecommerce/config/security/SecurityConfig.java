package com.example.ecommerce.config.security;

import com.example.ecommerce.config.security.filterchain.JwtAuthenticationFilter;
import com.example.ecommerce.config.security.filterchain.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    private final AuthenticationConfiguration authenticationConfiguration;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // AuthenticationManager 빈을 등록합니다.
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //1. csrf filter disable
        http.csrf(csrfCustomizer -> csrfCustomizer.disable());

        //2. session disable
        http.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //3. form login disable -> 기본적인 formlogin은 session기반의 stateful 방식이기에 disalbe 시킴
        http.formLogin(formLoginCustomizer -> formLoginCustomizer.disable());

        //4. http Basic disable
        http.httpBasic(httpBasicCustomizer -> httpBasicCustomizer.disable());

        //5. 권한에 따라 url mapping
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer.requestMatchers("/api/v1/user/**")
                                                                                                     .hasAnyAuthority("USER", "MANAGER", "ADMIN")
                                                                                                     .requestMatchers("/api/v1/manager/**")
                                                                                                     .hasAnyAuthority("MANAGER", "ADMIN")
                                                                                                     .requestMatchers("/api/v1/admin/**")
                                                                                                     .hasAuthority("ADMIN")
                                                                                                     .anyRequest()
                                                                                                     .permitAll());

        //6. corsFilter 설정 추가
        // 만약 Controller에 @CrossOrigin을 붙여주면 -> 인증이 없어도 되는 요청만 cors 정책에 안걸리게 되므로 -> 로그인을 해야 할 수 있는 요청들은 어차피 cors 정책에 걸리게 됨
        http.addFilter(corsFilter); // 모든 요청이 와도 -> 이 corssFilter를 거치게 되고 -> 이를 통해 cors 정책에 걸리지 않고, 요청이 서버까지 들어온다

        //7. JwtAuthenticationFilter를 AuthenticationFilter로 등록
        http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtSecret));

        //8.
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtSecret));

        return http.build();
    }
}
