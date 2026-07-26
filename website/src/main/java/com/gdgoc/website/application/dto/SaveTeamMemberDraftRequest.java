package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

public record SaveTeamMemberDraftRequest(

        String studentNumber,

        @Schema(example = "010-1234-5678")
        String phoneNumber,

        String major,

        Integer completedSemester,

        Boolean onLeave,

        String introduction,

        String motivation,

        String communityPerspective,

        String goal,

        String portfolioUrl,

        String project,

        String leadershipExperience,

        String techStack,

        String studyField,

        @Schema(example = "[\"STUDY_LEAD\", \"MANAGEMENT\", \"PROJECT_MENTOR\"]")
        Set<TeamMemberRole> roles,

        String eventIdea,

        @Schema(example = "[24, 25]")
        List<Long> interviewTimeSlotIds

) {
}
