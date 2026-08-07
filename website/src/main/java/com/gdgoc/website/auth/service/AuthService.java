package com.gdgoc.website.auth.service;

import com.gdgoc.website.auth.dto.LoginRequest;
import com.gdgoc.website.auth.dto.ReissueRequest;
import com.gdgoc.website.auth.dto.SendVerificationRequest;
import com.gdgoc.website.auth.dto.SignupRequest;
import com.gdgoc.website.auth.dto.TokenResponse;
import com.gdgoc.website.auth.dto.VerifyCodeRequest;
import com.gdgoc.website.auth.entity.EmailVerification;
import com.gdgoc.website.auth.entity.RefreshToken;
import com.gdgoc.website.auth.entity.TokenBlacklist;
import com.gdgoc.website.auth.repository.EmailVerificationRepository;
import com.gdgoc.website.auth.repository.RefreshTokenRepository;
import com.gdgoc.website.auth.repository.TokenBlacklistRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.global.security.JwtTokenProvider;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int VERIFICATION_TTL_MINUTES = 3;

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
     * 2. 이메일 인증번호 검증 (6자리 코드 일치 및 만료시간 확인)
     */
    @Transactional
    public void verifyEmailCode(String email, String inputCode) {
        EmailVerification verificationEntity = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException(ErrorCode.VERIFICATION_NOT_FOUND));

        if (verificationEntity.isExpired()) {
            throw new BaseException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!verificationEntity.getCode().equals(inputCode)) {
            throw new BaseException(ErrorCode.VERIFICATION_CODE_MISMATCH);
        }

        verificationEntity.markAsVerified();
    }

    /**
     * 3. 최종 회원가입 로직
     */
    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL);
        }

        validateVerifiedEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
            .email(request.email())
            .password(encodedPassword)
            .name(request.name())
            .build();

        return userRepository.save(user).getId();
    }

    /**
     * 4. 로그인 비즈니스 로직
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BaseException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        RefreshToken tokenEntity = refreshTokenRepository.findByEmail(user.getEmail())
            .orElseGet(() -> RefreshToken.builder()
                .email(user.getEmail())
                .token(refreshToken)
                .ttlMillis(jwtTokenProvider.getRefreshTokenValidityInMs())
                .build());

        tokenEntity.updateToken(refreshToken, jwtTokenProvider.getRefreshTokenValidityInMs());
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * 5. 로그아웃 비즈니스 로직 (RefreshToken 삭제 & AccessToken Blacklist 등록)
     */
    @Transactional
    public void logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new BaseException(ErrorCode.INVALID_AUTH_HEADER);
        }

        String accessToken = bearerToken.substring(7);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmail(accessToken);
        refreshTokenRepository.deleteByEmail(email);

        long remainMillis = jwtTokenProvider.getExpirationMillis(accessToken);
        if (remainMillis > 0) {
            TokenBlacklist blacklist = TokenBlacklist.builder()
                .token(accessToken)
                .expiresAt(LocalDateTime.now().plus(java.time.Duration.ofMillis(remainMillis)))
                .build();
            tokenBlacklistRepository.save(blacklist);
        }
    }

    /**
     * 6. 토큰 재발급 비즈니스 로직 (RTR: Refresh Token Rotation 적용)
     */
    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getEmail(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException(ErrorCode.LOGOUT_TOKEN));

        if (!savedToken.getToken().equals(refreshToken)) {
            refreshTokenRepository.delete(savedToken);
            throw new BaseException(ErrorCode.TOKEN_MISMATCH);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        savedToken.updateToken(newRefreshToken, jwtTokenProvider.getRefreshTokenValidityInMs());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    /**
     * 6자리 랜덤 난수 생성기 (100000 ~ 999999)
     */
    private String generateSixDigitCode() {
        int code = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 이메일 인증 완료 계정 검사 메서드
     */
    private void validateVerifiedEmail(String email) {
        EmailVerification verificationEntity = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException(ErrorCode.EMAIL_NOT_VERIFIED));

        if (!verificationEntity.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
    }
}