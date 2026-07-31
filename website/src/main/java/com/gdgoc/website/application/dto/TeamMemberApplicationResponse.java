package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.TeamMemberApplication;
import com.gdgoc.website.application.entity.TeamMemberRole;

import java.util.List;
import java.util.Set;

public record TeamMemberApplicationResponse(

        Long applicationId,

        ApplicationStatus applicationStatus,

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

    public static TeamMemberApplicationResponse from(
            Application application,
            TeamMemberApplication teamMemberApplication,
            List<Long> interviewTimeSlotIds
    ) {
        return new TeamMemberApplicationResponse(
                application.getId(),
                application.getApplicationStatus(),
                application.getStudentNumber(),
                application.getPhoneNumber(),
                application.getMajor(),
                application.getCompletedSemester(),
                application.getOnLeave(),
                teamMemberApplication.getIntroduction(),
                teamMemberApplication.getMotivation(),
                teamMemberApplication.getCommunityPerspective(),
                teamMemberApplication.getGoal(),
                teamMemberApplication.getPortfolioUrl(),
                teamMemberApplication.getProject(),
                teamMemberApplication.getLeadershipExperience(),
                teamMemberApplication.getTechStack(),
                teamMemberApplication.getStudyField(),
                Set.copyOf(teamMemberApplication.getRoles()),
                teamMemberApplication.getEventIdea(),
                interviewTimeSlotIds
        );
    }
}
