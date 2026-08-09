package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import java.util.List;
import java.util.Set;

public record SaveMemberDraftRequest(

        String studentNumber,

        @Schema(example = "010-1234-5678")
        String phoneNumber,

        String major,

        Integer completedSemester,

        Boolean onLeave,

        String introduction,

        String motivation,
        
        // 얻어가고자 하는 것
        String goal,
        
        String portfolioUrl,
        
        // 경력 / 활동 사항
        String experience,
        
        String project,

        String studyInterest,
        
        String projectIdea,

        String techStack,
        
        @Schema(example = "[29, 30]")
        List<Long> interviewTimeSlotIds
) {
}
