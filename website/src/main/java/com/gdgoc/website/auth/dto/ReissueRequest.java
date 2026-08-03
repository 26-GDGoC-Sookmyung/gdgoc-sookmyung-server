package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "JWT 토큰 재발급 요청 DTO")
public record ReissueRequest(
        @NotBlank(message = "Refresh Token을 입력해주세요.")
        @Schema(description = "로그인 시 발급받은 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String refreshToken
) {}