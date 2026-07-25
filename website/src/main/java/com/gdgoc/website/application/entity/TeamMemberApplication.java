package com.gdgoc.website.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMemberApplication {

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

    // 숙명여대 개발자 커뮤니티에 대한 생각
    @Lob
    private String communityPerspective;

    // 얻어가고자 하는 것
    @Lob
    private String goal;

    private String portfolioUrl;

    @Lob
    private String project;

    // 진행 / 커뮤니티 경험
    @Lob
    private String leadershipExperience;

    @Lob
    private String techStack;

    @Lob
    private String studyField;

    // 가능한 역할
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "team_member_application_role",
            joinColumns = @JoinColumn(name = "team_member_application_id")
    )
    @Column(name = "role")
    private Set<TeamMemberRole> roles = new HashSet<>();

    @Lob
    private String eventIdea;

    public TeamMemberApplication(
            Application application,
            String introduction,
            String motivation,
            String communityPerspective,
            String goal,
            String portfolioUrl,
            String project,
            String leadershipExperience,
            String techStack,
            String studyField,
            Set<TeamMemberRole> roles,
            String eventIdea
    ) {
        this.application = application;
        this.introduction = introduction;
        this.motivation = motivation;
        this.communityPerspective = communityPerspective;
        this.goal = goal;
        this.portfolioUrl = portfolioUrl;
        this.project = project;
        this.leadershipExperience = leadershipExperience;
        this.techStack = techStack;
        this.studyField = studyField;
        this.roles = roles == null
                ? new HashSet<>()
                : new HashSet<>(roles);
        this.eventIdea = eventIdea;
    }

    public void update(
            String introduction,
            String motivation,
            String communityPerspective,
            String goal,
            String portfolioUrl,
            String project,
            String leadershipExperience,
            String techStack,
            String studyField,
            Set<TeamMemberRole> roles,
            String eventIdea
    ) {
        this.introduction = introduction;
        this.motivation = motivation;
        this.communityPerspective = communityPerspective;
        this.goal = goal;
        this.portfolioUrl = portfolioUrl;
        this.project = project;
        this.leadershipExperience = leadershipExperience;
        this.techStack = techStack;
        this.studyField = studyField;
        this.roles = roles == null
                ? new HashSet<>()
                : new HashSet<>(roles);
        this.eventIdea = eventIdea;
    }

}
