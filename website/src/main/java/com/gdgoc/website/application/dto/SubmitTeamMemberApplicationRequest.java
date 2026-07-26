package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public record SubmitTeamMemberApplicationRequest(

        @NotBlank
        String studentNumber,

        @NotBlank
        @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$")
        String phoneNumber,

        @NotBlank
        String major,

        @NotNull
        @Min(1)
        Integer completedSemester,

        @NotNull
        Boolean onLeave,

        @NotBlank
        String introduction,

        @NotBlank
        String motivation,

        @NotBlank
        String communityPerspective,

        @NotBlank
        String goal,

        @NotBlank
        String portfolioUrl,

        @NotBlank
        String project,

        @NotBlank
        String leadershipExperience,

        @NotBlank
        String techStack,

        @NotBlank
        String studyField,

        @NotEmpty
        Set<TeamMemberRole> roles,

        String eventIdea,

        @NotEmpty
        List<Long> interviewTimeSlotIds

) {
}
