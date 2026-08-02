package com.gdgoc.website.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "역할")
public enum MemberRole {
    ORGANIZER("Organizer", 1),
    TEAM_MEMBER("Team Member", 2),
    MEMBER("Member", 3);

    private final String displayName;
    private final int priority;

    MemberRole(String displayName, int priority) {
        this.displayName = displayName;
        this.priority = priority;
    }
}