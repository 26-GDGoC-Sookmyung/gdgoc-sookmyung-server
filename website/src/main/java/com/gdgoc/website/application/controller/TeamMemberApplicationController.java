package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.SaveTeamMemberDraftRequest;
import com.gdgoc.website.application.dto.SubmitTeamMemberApplicationRequest;
import com.gdgoc.website.application.service.TeamMemberApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications/team-member")
@Tag(
        name = "Team Member Application",
        description = "Team Member 지원서 관련 API"
)
public class TeamMemberApplicationController {
    private final TeamMemberApplicationService teamMemberApplicationService;

    // TODO : 추후 로그인 기능 구현 시 @AuthenticationPrincipal 추가

    @PutMapping("/draft")
    @Operation(summary = "Team Member 지원서 임시저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, APPLICATION_ALREADY_SUBMITTED, INVALID_INTERVIEW_TIME_SLOT)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> saveTeamMemberDraft(@RequestBody SaveTeamMemberDraftRequest teamMemberDraft) {
        teamMemberApplicationService.saveTeamMemberDraft(1L, teamMemberDraft);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @PostMapping("/submit")
    @Operation(summary = "Team Member 지원서 제출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, APPLICATION_ALREADY_SUBMITTED, INVALID_INTERVIEW_TIME_SLOT)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> submitTeamMemberApplicaiton(@Valid @RequestBody SubmitTeamMemberApplicationRequest teamMemberApplication) {
        teamMemberApplicationService.submitTeamMemberApplication(1L, teamMemberApplication);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

}
