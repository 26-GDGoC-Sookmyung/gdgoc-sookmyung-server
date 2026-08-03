package com.gdgoc.website.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.global.response.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        log.warn("인증 실패 (401 Unauthorized) - URI: {}, Message: {}", 
                 request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        BaseResponse<?> errorResponse = BaseResponse.error(
                ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}