package com.example.ecommerce.config.security.filterchain;

import com.example.ecommerce.domain.user.User;
import com.example.ecommerce.domain.user.dto.response.SignInResponseDto;
import com.example.ecommerce.global.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final String jwtSecret;

    // 인증 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("**** 1. 로그인 시도 - JwtAuthenticationFilter 진입 *****");

        //1. request에 있는 username, password를 꺼내서 -> UsernamePasswordAuthenticationToken을 생성하여 -> AuthenticationManager에게 넘기면서 인증 요청
        HttpServletRequest httpServletRequest = request;
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    // 인증 처리 후 -> 인증에 성공했을 때 callback -> JWT을 생성해서 응답
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        System.out.println("**** 3_1. 로그인 시도 - 로그인 성공 *****");

        //1. 인증에 성공한 Authentication에서 UserDetailsService를 꺼내고 -> 그 안에서 User Entity를 꺼낸다
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        User user = principalDetails.getUser();

        //2. user의 id, username, authority 정보를 claim으로 담는 JWT를 생성한다
        String jwt = Jwts.builder()
                         .setSubject(String.valueOf(user.getId())) // 토큰의 주제 설정 (여기서는 userId 사용)
                         .setExpiration(new Date(System.currentTimeMillis() + (60000*10))) // 토큰의 만료 시간 설정 (여기서는 현재로부터 10분)
                         .claim("username", user.getUsername())
                         .claim("authority", user.getAuthority().getValue()) // 권한(역할) 정보 포함
                         .signWith(SignatureAlgorithm.HS512, Base64.encodeBase64String(jwtSecret.getBytes())) // HS512 알고리즘과 비밀 키를 사용하여 서명
                         .compact();

        //3. 생성한 JWT를 Response의 Authorization Header에 담는다
        HttpServletResponse httpServletResponse = response;
        addJwtToResponseBody(httpServletResponse, SignInResponseDto.toDto(jwt));

        System.out.println("***** 4. 로그인 시도 - 로그인에 성공한 사용자 정보를 담아 생성한 JWT를 응답으로 담아 보냄 *****");
    }

    private void addJwtToResponseBody(HttpServletResponse httpServletResponse, SignInResponseDto signInResponseDto) {

        try {

            // ObjectMapper 인스턴스 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // DTO 객체를 JSON 문자열로 변환
            String jsonResponse = objectMapper.writeValueAsString(signInResponseDto);

            // 응답의 컨텐트 타입 설정
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");

            // JSON 문자열을 응답 본문에 추가
            httpServletResponse.getWriter().write(jsonResponse);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 인증 처리 후 -> 인증에 실패했을 때 callback
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {

        System.out.println("**** 3_2. 로그인 시도 - 로그인 실패 *****");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
