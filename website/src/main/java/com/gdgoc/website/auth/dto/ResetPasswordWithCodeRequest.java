package com.gdgoc.website.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "인증번호 기반 비밀번호 재설정 요청 DTO")
public record ResetPasswordWithCodeRequest(
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Schema(description = "가입된 이메일 주소", example = "user@sookmyung.ac.kr")
    String email,

    @NotBlank(message = "인증번호를 입력해주세요.")
    @Schema(description = "이메일로 받은 6자리 인증번호", example = "123456")
    String code,

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Schema(description = "새로 설정할 비밀번호", example = "NewPassword456!")
    String newPassword
) {
}