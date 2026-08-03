package com.gdgoc.website.global.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 로그인한 사용자 인증 정보 DTO")
public record CurrentUser(
        @Schema(description = "유저 PK", example = "1")
        Long id,
        @Schema(description = "이메일", example = "abc@gmail.com")
        String email,
        @Schema(description = "사용자 이름", example = "김숙명")
        String name
) {}