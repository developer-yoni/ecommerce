package com.example.ecommerce.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {

    private final String AUTHORIZATION = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //0. 일단 기본 로그는 찍고
        System.out.println("***** 필터 1 *****");

        // Bearer 인증 방식에서 Token을 얻기 위한 전처리
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : "UnAuthorized";

        //1. Bearer 인증방식에서 Token 값이 yoni가 아니면 -> 401 에러
        if (!token.equals("yoni")) {

            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
            return;
        }

        //2. Bearer 인증방식에서 Token 값이 yoni가 맞다면 -> 로그 찍고 -> 다음 filter 동작
        System.out.println("***** " + authorizationHeader + " *****");
        chain.doFilter(request, response);
    }
}
