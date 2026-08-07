package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT 토큰 응답 DTO")
public record TokenResponse(
        @Schema(description = "Bearer Access Token (30분 유효)", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String accessToken,

        @Schema(description = "RefreshToken (7일 유효)", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String refreshToken
) {}