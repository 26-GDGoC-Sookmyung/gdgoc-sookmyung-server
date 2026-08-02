package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 DTO")
public record SignupRequest(
        @NotBlank
        @Email
        @Schema(description = "이메일", example = "abc@gmail.com")
        String email,

        @NotBlank
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이내여야 합니다.")
        @Schema(description = "비밀번호", example = "Password123!")
        String password,

        @NotBlank
        @Schema(description = "이름(실명)", example = "김숙명")
        String name
) {}