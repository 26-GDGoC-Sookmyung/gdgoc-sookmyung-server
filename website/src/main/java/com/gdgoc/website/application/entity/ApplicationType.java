package com.gdgoc.website.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum ApplicationType {

    TEAM_MEMBER(
            LocalDateTime.of(2026, 8, 10, 0, 0),
            LocalDateTime.of(2026, 8, 19, 0, 0)
    ),

    MEMBER(
            LocalDateTime.of(2026, 8, 10, 0, 0),
            LocalDateTime.of(2026, 8, 25, 0, 0)
    );

    private final LocalDateTime recruitmentStartAt;
    private final LocalDateTime recruitmentEndAt;

}
