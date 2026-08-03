package com.gdgoc.website.user.entity;

import com.gdgoc.website.global.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@Schema(description = "회원 계정")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "PK", example = "1")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Schema(description = "이메일", example = "abc@gmail.com")
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @Schema(hidden = true)
    private String password;

    @Column(name = "name", nullable = false, length = 30)
    @Schema(description = "이름", example = "김숙명")
    private String name;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}