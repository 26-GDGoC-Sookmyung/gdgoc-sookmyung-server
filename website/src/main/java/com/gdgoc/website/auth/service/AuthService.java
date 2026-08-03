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
import java.time.LocalDateTime;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtTokenProvider jwtTokenProvider;

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

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "가입되지 않은 이메일이거나 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "가입되지 않은 이메일이거나 비밀번호가 올바르지 않습니다.");
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
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "올바른 Bearer 토큰이 아닙니다.");
        }

        String accessToken = bearerToken.substring(7);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않거나 이미 만료된 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmail(accessToken);
        refreshTokenRepository.deleteByEmail(email);

        long remainMillis = jwtTokenProvider.getExpirationMillis(accessToken);
        if (remainMillis > 0) {
            TokenBlacklist blacklist = TokenBlacklist.builder()
                .token(accessToken)
                .expiresAt(LocalDateTime.now().plusNanos(remainMillis * 1000000L))
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
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않거나 만료된 Refresh Token입니다. 다시 로그인해주세요.");
        }

        String email = jwtTokenProvider.getEmail(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "로그아웃된 계정이거나 일치하는 토큰 내역이 없습니다."));

        if (!savedToken.getToken().equals(refreshToken)) {
            refreshTokenRepository.delete(savedToken);
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "토큰 정보가 일치하지 않습니다. 다시 로그인해주세요.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        savedToken.updateToken(newRefreshToken, jwtTokenProvider.getRefreshTokenValidityInMs());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}