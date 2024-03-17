package com.example.ecommerce.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
// 스프링 시큐리티 필터가 -> 스프링 기본 필터체인에 등록이 된다
// 그리고 그 시큐리티 필터란, 이 SecurityConfig의 설정 정보에 따라 적절하게 생성된다
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * [BCryptPasswordEncoder 빈 등록] : password encoder
     * */
    // 추가로 @Bean에 의해, 해당 메소드에 의해 리턴되는 객체를 Spring Bean으로 등록해준다
    @Bean
    public BCryptPasswordEncoder encodePwd() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf 필터는 disable
        http.csrf(AbstractHttpConfigurer::disable);

        // /user/** , /manager/** , /admin/** 은 인증과 동시에 권한을 요구하도록 설정 + 그외는 인증 없어도 됨
        http.authorizeHttpRequests(authorizeRequest -> authorizeRequest.requestMatchers("/user/**")
                                                                       .authenticated()
                                                                       .requestMatchers("/manager/**")
                                                                       .hasAnyAuthority("ADMIN", "MANAGER")
                                                                       .requestMatchers("/admin/**")
                                                                       .hasAuthority("ADMIN")
                                                                       .anyRequest().permitAll());

        // [새로움] 이 설정을 추가함으로써 -> /user , /manager, /admin 으로 인증 없이 이동하려고 하면, /login 페이지로 리다이렉트 된다
        http.formLogin(formLogin -> formLogin.loginPage("/loginForm"));

        return http.build();
    }
}
