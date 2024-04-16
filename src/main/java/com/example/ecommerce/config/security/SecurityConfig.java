package com.example.ecommerce.config.security;

import com.example.ecommerce.global.oauth.PrincipalOauth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
// 스프링 시큐리티 필터가 -> 스프링 기본 필터체인에 등록이 된다
// 그리고 그 시큐리티 필터란, 이 SecurityConfig의 설정 정보에 따라 적절하게 생성된다
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) //@Secured 어노테이션을 활성화 하겠다 + @PreAuthorize, @PostAuthorize 어노테이션 활성화

public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

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
        // [추가] : /login url로 요청이 오면 -> security가 대신 낚아채서 로그인을 진행해준다 -> 그래서 login용 Api를 만들 필요가 없다 -> loginProcessingUrl("/login")
        // [추가] : 이후 로그인에 성공하면 메인 페이지로 이동하게 한다 by defaultSuccessUrl()
        http.formLogin(formLogin -> formLogin.loginPage("/loginForm")
                                              .loginProcessingUrl("/login")
                                              .defaultSuccessUrl("/"));

        // [이 설정의 역할]
        // 1. OAuth2 로그인 기능을 활성화
        // - 따라서 Spring Security는 /oauth2/authorization/google 같은 엔드포인트를 통해
        // 외부 OAuth2 제공자로의 리다이렉션을 처리할 준비가 된 것입니다.
        // 따라서, 사용자가 "구글 로그인" 링크를 클릭할 때, Spring Security는 구성된 클라이언트 등록 정보를 바탕으로 사용자를 구글의 인증 페이지로 리다이렉션할 수 있게 된다.

        // 2.사용자가 인증이 필요할 때 지정된 사용자 정의 로그인 페이지(/loginForm)로 리다이렉션한다.
        // - Oauth2 로그인과 무슨 상관인지는 잘 모르겠음.
        http.oauth2Login(oauth2Login -> oauth2Login.loginPage("/loginForm")
                                                   .userInfoEndpoint(customizer -> customizer.userService(principalOauth2UserService)));
        // 카카오의 경우
        // 1. 인증 (code 받기) 2. 권한 부여 (accessToken 받기) 3. 사용자 프로필 정보 가져오기 (accessToken을 가지고 리소스 서버에 접근해서)
        // 4_1. 그 정보를 토대로 -> 회원가입을 자동으로 진행시키도 하고
        // 4_2. 그 정보 이외에 추가하고 싶다면 -> 추가 정보를 받아야 비로소 회원가입이 완료되도록 하자

        // 구글의 경우는
        // 1. 인증에 성공하면 code를 받는게 아니라 , (accessToken + 회원 정보)를 한방에 받는다


        return http.build();
    }
}
