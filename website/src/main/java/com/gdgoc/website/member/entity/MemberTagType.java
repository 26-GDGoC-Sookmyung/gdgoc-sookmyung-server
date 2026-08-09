package com.gdgoc.website.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "스터디 및 스택 태그")
public enum MemberTagType {
    FLUTTER("Flutter"),
    FRONTEND("Frontend"),
    BACKEND("Backend"),
    AI_ML("AI/ML"),
    DEV_REL("DevRel"),
    REACT("React"),
    APP_WEB("App/Web"),
    CLOUD("Cloud"),
    SPRING("Spring");

    private final String displayName;

    MemberTagType(String displayName) {
        this.displayName = displayName;
    }

}