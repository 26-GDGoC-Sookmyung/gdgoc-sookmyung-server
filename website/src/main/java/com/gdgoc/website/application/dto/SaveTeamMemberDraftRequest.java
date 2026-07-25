package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;

import java.util.List;
import java.util.Set;

public record SaveTeamMemberDraftRequest(

        String studentNumber,

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

        Set<TeamMemberRole> roles,

        String eventIdea,

        List<Long> interviewTimeSlotIds

) {
}
