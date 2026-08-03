package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(
        @NotBlank
        @Email
        @Schema(description = "이메일", example = "abc@sookmyung.ac.kr")
        String email,

        @NotBlank
        @Schema(description = "비밀번호", example = "Password123!")
        String password
) {}