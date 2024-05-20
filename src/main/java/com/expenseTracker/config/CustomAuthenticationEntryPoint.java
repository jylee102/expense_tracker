package com.expenseTracker.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // 인증되지 않은 사용자가 페이지나 리소스를 요청할 경우 어떻게 처리할지
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        if (authException != null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect("/members/login"); // 로그인 페이지로 리디렉션
        }
    }
}
