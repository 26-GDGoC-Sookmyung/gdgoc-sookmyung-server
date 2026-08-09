package com.gdgoc.website.member.dto;

import com.gdgoc.website.member.entity.Member;
import com.gdgoc.website.member.entity.MemberTagType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "멤버 정보 응답 DTO")
public record MemberResponse(
    @Schema(description = "멤버 PK", example = "1")
    Long id,

    @Schema(description = "이름", example = "김숙명")
    String name,

    @Schema(description = "역할", example = "Organizer")
    String role,

    @Schema(description = "프로필 이미지 URL", example = "https://cloudflare/member-1.png")
    String profileImageUrl,

    @Schema(description = "기술 및 파트 태그 리스트 (화면 표시용)", example = "[\"Flutter\", \"Frontend\"]")
    List<String> tags
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getRole().getDisplayName(),
            member.getProfileImageUrl(),
            member.getTags().stream()
                  .map(MemberTagType::getDisplayName)
                  .toList()
        );
    }
}