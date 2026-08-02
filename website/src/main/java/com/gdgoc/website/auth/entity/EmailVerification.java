package com.gdgoc.website.auth.entity;

import com.gdgoc.website.global.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "email_verifications",
    indexes = @Index(name = "idx_email_verifications_email", columnList = "email")
)
@Schema(description = "이메일 인증")
public class EmailVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "PK", example = "1")
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    @Schema(description = "인증 요청 이메일 주소", example = "abc@gmail.com")
    private String email;

    @Column(name = "code", nullable = false, length = 10)
    @Schema(description = "6자리 랜덤 인증번호", example = "849201")
    private String code;

    @Column(name = "is_verified", nullable = false)
    @Schema(description = "인증 성공 여부", example = "false")
    private boolean isVerified = false;

    @Column(name = "expires_at", nullable = false)
    @Schema(description = "인증번호 만료 일시 (생성일시 + 3분)", example = "2026-08-03T00:06:00")
    private LocalDateTime expiresAt;

    @Builder
    public EmailVerification(String email, String code, int ttlMinutes) {
        this.email = email;
        this.code = code;
        this.expiresAt = LocalDateTime.now().plusMinutes(ttlMinutes);
        this.isVerified = false;
    }

    // 만료 시간 검증
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    // 인증 통과 처리
    public void markAsVerified() {
        this.isVerified = true;
    }
}