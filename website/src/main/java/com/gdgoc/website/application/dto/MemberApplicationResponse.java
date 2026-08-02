package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.MemberApplication;

import java.util.List;

public record MemberApplicationResponse(

        Long applicationId,

        ApplicationStatus applicationStatus,

        String studentNumber,
        String phoneNumber,
        String major,
        Integer completedSemester,
        Boolean onLeave,

        String introduction,
        String motivation,
        String goal,

        String portfolioUrl,
        String experience,
        String project,
        String studyInterest,
        String projectIdea,
        String techStack,

        List<Long> interviewTimeSlotIds

) {

    public static MemberApplicationResponse of(
            Application application,
            MemberApplication memberApplication,
            List<Long> interviewTimeSlotIds
    ) {
        return new MemberApplicationResponse(
                application.getId(),
                application.getApplicationStatus(),
                application.getStudentNumber(),
                application.getPhoneNumber(),
                application.getMajor(),
                application.getCompletedSemester(),
                application.getOnLeave(),

                memberApplication.getIntroduction(),
                memberApplication.getMotivation(),
                memberApplication.getGoal(),

                memberApplication.getPortfolioUrl(),
                memberApplication.getExperience(),
                memberApplication.getProject(),
                memberApplication.getStudyInterest(),
                memberApplication.getProjectIdea(),
                memberApplication.getTechStack(),

                interviewTimeSlotIds
        );
    }

}
