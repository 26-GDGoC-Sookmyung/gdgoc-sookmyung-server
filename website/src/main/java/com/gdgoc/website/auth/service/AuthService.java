package com.gdgoc.website.auth.service;

import com.gdgoc.website.auth.dto.SendVerificationRequest;
import com.gdgoc.website.auth.dto.SignupRequest;
import com.gdgoc.website.auth.dto.VerifyCodeRequest;
import com.gdgoc.website.auth.entity.EmailVerification;
import com.gdgoc.website.auth.repository.EmailVerificationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    // TODO 이메일 인증번호
    /**
     * 1. 이메일 인증번호 발송 API
     */
    @Transactional
    public void sendVerificationCode(SendVerificationRequest request) {
        // 이미 가입된 이메일인지 검사
        if (userRepository.existsByEmail(request.email())) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 가입된 이메일입니다.");
        }

        // 인증코드 생성 및 메일 발송
        String code = mailService.generateVerificationCode();
        mailService.sendEmail(request.email(), code);

        // DB 저장 (TTL 3분)
        EmailVerification verification = EmailVerification.builder()
            .email(request.email())
            .code(code)
            .ttlMinutes(3)
            .build();
        emailVerificationRepository.save(verification);
    }

    /**
     * 2. 인증번호 맞는지 확인(검증) 로직
     */
    @Transactional
    public void verifyEmailCode(VerifyCodeRequest request) {
        EmailVerification verification = emailVerificationRepository
            .findTopByEmailOrderByCreatedAtDesc(request.email())
            .orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "인증 요청 내역이 없습니다."));

        if (verification.isExpired()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "인증번호가 만료되었습니다. 다시 시도해주세요.");
        }

        if (!verification.getCode().equals(request.code())) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "인증번호가 일치하지 않습니다.");
        }

        verification.markAsVerified();
    }

    /**
     * 3. 최종 회원가입 로직
     */
    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 가입된 이메일입니다.");
        }

        EmailVerification verification = emailVerificationRepository
            .findTopByEmailOrderByCreatedAtDesc(request.email())
            .orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이메일 인증을 먼저 진행해주세요."));

        if (!verification.isVerified()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이메일 인증이 완료되지 않은 계정입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
            .email(request.email())
            .password(encodedPassword)
            .name(request.name())
            .build();

        return userRepository.save(user).getId();
    }
}