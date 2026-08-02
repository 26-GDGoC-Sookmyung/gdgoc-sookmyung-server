package com.gdgoc.website.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberApplication {

    // applicationId를 PK로
    @Id
    @Column(name = "application_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String motivation;

    // 얻어가고자 하는 것
    @Column(columnDefinition = "TEXT")
    private String goal;

    private String portfolioUrl;

    // 경력 / 활동 사항
    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String project;

    @Column(columnDefinition = "TEXT")
    private String studyInterest;

    @Column(columnDefinition = "TEXT")
    private String projectIdea;

    @Column(columnDefinition = "TEXT")
    private String techStack;

}
