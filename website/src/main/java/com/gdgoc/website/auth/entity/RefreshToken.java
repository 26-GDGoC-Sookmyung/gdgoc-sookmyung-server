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
@Table(name = "refresh_tokens", indexes = @Index(name = "idx_refresh_tokens_email", columnList = "email"))
@Schema(description = "JWT Refresh Token 저장 엔티티")
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public RefreshToken(String email, String token, long ttlMillis) {
        this.email = email;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusNanos(ttlMillis * 1000000L);
    }

    public void updateToken(String newToken, long ttlMillis) {
        this.token = newToken;
        this.expiresAt = LocalDateTime.now().plusNanos(ttlMillis * 1000000L);
    }
}