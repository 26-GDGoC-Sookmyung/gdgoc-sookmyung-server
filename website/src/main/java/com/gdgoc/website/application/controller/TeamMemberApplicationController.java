package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.SaveTeamMemberDraftRequest;
import com.gdgoc.website.application.dto.SubmitTeamMemberApplicationRequest;
import com.gdgoc.website.application.dto.TeamMemberApplicationResponse;
import com.gdgoc.website.application.dto.TeamMemberInterviewTimeSlotResponse;
import com.gdgoc.website.application.service.TeamMemberApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.global.security.AuthUser;
import com.gdgoc.website.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications/team-member")
@Tag(
        name = "Team Member Application",
        description = "Team Member 지원서 관련 API"
)
public class TeamMemberApplicationController {
    private final TeamMemberApplicationService teamMemberApplicationService;

    @PutMapping("/draft")
    @Operation(summary = "Team Member 지원서 임시저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, APPLICATION_ALREADY_SUBMITTED, INVALID_INTERVIEW_TIME_SLOT, APPLICATION_PERIOD_CLOSED)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> saveTeamMemberDraft(@RequestBody SaveTeamMemberDraftRequest teamMemberDraft,
                                                               @AuthUser CurrentUser currentUser) {
        teamMemberApplicationService.saveTeamMemberDraft(currentUser.id(), teamMemberDraft);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @PostMapping("/submit")
    @Operation(summary = "Team Member 지원서 제출")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, INVALID_INTERVIEW_TIME_SLOT, APPLICATION_PERIOD_CLOSED)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> submitTeamMemberApplication(@Valid @RequestBody SubmitTeamMemberApplicationRequest teamMemberApplication,
                                                                       @AuthUser CurrentUser currentUser) {
        teamMemberApplicationService.submitTeamMemberApplication(currentUser.id(), teamMemberApplication);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @GetMapping("/interview-time")
    @Operation(summary = "Team Member 면접시간대 조회")
    public ResponseEntity<BaseResponse<?>> getTeamMemberInterviewTimeSlots() {
        List<TeamMemberInterviewTimeSlotResponse> interviewTimeSlots = teamMemberApplicationService.getTeamMemberInterviewTimeSlots();
        return ResponseEntity.ok(BaseResponse.success(interviewTimeSlots));
    }

    @GetMapping
    @Operation(summary = "기존에 작성하던 / 제출한 Team Member 지원서 내용 조회",
            description = "작성한 지원서가 없는 경우 data는 null로 반환됩니다.")
    public ResponseEntity<BaseResponse<?>> getTeamMemberApplication(@AuthUser CurrentUser currentUser) {
        TeamMemberApplicationResponse teamMemberApplication = teamMemberApplicationService.getTeamMemberApplication(currentUser.id());
        return ResponseEntity.ok(BaseResponse.success(teamMemberApplication));
    }

}
