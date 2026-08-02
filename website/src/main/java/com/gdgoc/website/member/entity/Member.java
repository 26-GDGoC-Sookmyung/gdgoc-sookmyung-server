package com.gdgoc.website.member.entity;

import com.gdgoc.website.global.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
@Schema(description = "소속 멤버")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    @Schema(description = "부원 이름", example = "김숙명")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    @Schema(description = "동아리 내 직책 (ORGANIZER, TEAM_MEMBER, MEMBER)", example = "TEAM_MEMBER")
    private MemberRole role;

    @Column(name = "profile_image_url", length = 512)
    @Schema(description = "Cloudflare R2 프로필 사진 URL", example = "https://cdn.your-club.kr/profiles/member-1.png")
    private String profileImageUrl;

    @Column(name = "generation", nullable = false)
    @Schema(description = "활동 기수", example = "2")
    private int generation;

    @Column(name = "is_visible", nullable = false)
    @Schema(description = "멤버 목록 표시 여부", example = "true")
    private boolean isVisible;

    @Column(name = "user_id")
    @Schema(description = "가입 회원이 부원이 된 경우 해당 User PK (Nullable)", example = "10")
    private Long userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "jsonb")
    @Schema(description = "보유 스킬 및 파트 태그 목록", example = "[\"FLUTTER\", \"FRONTEND\"]")
    private List<MemberTagType> tags = new ArrayList<>();

    @Builder
    public Member(String name, MemberRole role, String profileImageUrl, int generation, Long userId, List<MemberTagType> tags) {
        this.name = name;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.generation = generation;
        this.userId = userId;
        this.tags = (tags != null) ? tags : new ArrayList<>();
        this.isVisible = true;
    }

    // 편의 메서드
    public void addTag(MemberTagType tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void removeTag(MemberTagType tag) {
        this.tags.remove(tag);
    }
}