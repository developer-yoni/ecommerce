package com.example.ecommerce.config.security.filterchain;

import com.example.ecommerce.domain.user.enums.Authority;
import com.example.ecommerce.global.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final String jwtSecret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String jwtSecret) {

        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        // /signup 경로에 대한 요청은 필터 처리를 건너뜁니다.
        if (requestURI.equals("/signup")) {

            chain.doFilter(request, response);
            return;
        }


        System.out.println("***** 권한처리 1. 인증이나 권한이 필요한 요청이 옴 *****");

        // 1. Bearer 인증 방식에서 Token을 얻기 위한 전처리
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String authorizationHeader =
                StringUtils.hasText(httpServletRequest.getHeader("Authorization")) ? httpServletRequest.getHeader("Authorization") : "";
        String accessToken = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : "UnAuthorized";
        System.out.println("***** accessToken : " + accessToken + " *****");

        // 2.토큰 해석
        try {

            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(Base64.encodeBase64String(jwtSecret.getBytes()))
                                .build()
                                .parseClaimsJws(accessToken)
                                .getBody();

            // 2_1 권한 체크를 위해 Authentication 객체를 넣어줌
            Long id = Long.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            Authority authority = Authority.convert(claims.get("authority", String.class));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(PrincipalDetails.successAuthorize(id, username, authority),
                                                              null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println("***** 권한처리 2_1. 올바른 사용자 *****");
            chain.doFilter(request, response);
        } catch (Exception e) {

            System.out.println("***** 권한처리 2_2. 올바르지 않은 사용자 *****");
            // Invalid token
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
        }
    }
}
