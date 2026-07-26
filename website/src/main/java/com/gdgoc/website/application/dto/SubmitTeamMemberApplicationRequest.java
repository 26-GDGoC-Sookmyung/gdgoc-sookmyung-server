package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public record SubmitTeamMemberApplicationRequest(

        @NotBlank
        @Pattern(
                regexp = "^\\d{7}$",
                message = "학번은 숫자 7자리여야 합니다."
        )
        String studentNumber,

        @NotBlank
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$",
                message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
        @Schema(example = "010-1234-5678")
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
        @Schema(example = "[\"STUDY_LEAD\", \"MANAGEMENT\", \"PROJECT_MENTOR\"]")
        Set<TeamMemberRole> roles,

        String eventIdea,

        @NotEmpty
        @Schema(example = "[24, 25]")
        List<Long> interviewTimeSlotIds

) {
}
