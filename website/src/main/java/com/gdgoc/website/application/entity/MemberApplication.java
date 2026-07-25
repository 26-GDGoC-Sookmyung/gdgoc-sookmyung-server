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

    @Lob
    private String introduction;

    @Lob
    private String motivation;

    // 얻어가고자 하는 것
    @Lob
    private String goal;

    private String portfolioUrl;

    // 경력 / 활동 사항
    @Lob
    private String experience;

    @Lob
    private String project;

    @Lob
    private String studyInterest;

    @Lob
    private String projectIdea;

    @Lob
    private String techStack;

}
