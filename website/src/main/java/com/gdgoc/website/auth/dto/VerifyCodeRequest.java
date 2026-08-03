package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 인증번호 검증 요청 DTO")
public record VerifyCodeRequest(
        @NotBlank
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Schema(description = "이메일 주소", example = "abc@gmail.com")
        String email,

        @NotBlank
        @Schema(description = "메일로 전송된 6자리 인증코드", example = "849201")
        String code
) {}