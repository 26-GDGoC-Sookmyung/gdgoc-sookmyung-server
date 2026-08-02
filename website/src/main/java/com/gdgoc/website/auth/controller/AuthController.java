package com.gdgoc.website.auth.controller;

import com.gdgoc.website.auth.dto.SendVerificationRequest;
import com.gdgoc.website.auth.dto.SignupRequest;
import com.gdgoc.website.auth.dto.VerifyCodeRequest;
import com.gdgoc.website.auth.service.AuthService;
import com.gdgoc.website.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "회원가입, 로그인 및 이메일 인증 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "이메일 인증코드 발송", description = "회원가입할 이메일로 6자리 인증번호를 발송합니다.")
    @PostMapping("/email/send")
    public ResponseEntity<BaseResponse<String>> sendVerificationCode(
        @RequestBody @Valid SendVerificationRequest request
    ) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok(
            BaseResponse.success("인증메일이 성공적으로 발송되었습니다.", request.email())
        );
    }

    @Operation(summary = "이메일 인증번호 검증", description = "메일로 받은 6자리 코드를 입력하여 인증을 완료합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<String>> verifyEmailCode(
        @RequestBody @Valid VerifyCodeRequest request
    ) {
        authService.verifyEmailCode(request);
        return ResponseEntity.ok(
            BaseResponse.success("이메일 인증이 성공적으로 완료되었습니다.", request.email())
        );
    }

    @Operation(summary = "회원가입", description = "이메일 인증이 통과된 사용자만 최종 가입을 완료합니다.")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Long>> signup(
        @RequestBody @Valid SignupRequest request
    ) {
        Long userId = authService.signup(request);
        return ResponseEntity.ok(
            BaseResponse.success("회원가입이 성공적으로 완료되었습니다.", userId)
        );
    }
}