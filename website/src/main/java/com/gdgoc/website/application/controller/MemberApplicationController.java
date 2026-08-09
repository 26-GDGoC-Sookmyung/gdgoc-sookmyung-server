package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.MemberApplicationResponse;
import com.gdgoc.website.application.dto.MemberInterviewTimeSlotResponse;
import com.gdgoc.website.application.dto.SaveMemberDraftRequest;
import com.gdgoc.website.application.dto.SaveTeamMemberDraftRequest;
import com.gdgoc.website.application.dto.SubmitMemberApplicationRequest;
import com.gdgoc.website.application.dto.SubmitTeamMemberApplicationRequest;
import com.gdgoc.website.application.dto.TeamMemberInterviewTimeSlotResponse;
import com.gdgoc.website.application.service.MemberApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.global.security.AuthUser;
import com.gdgoc.website.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications/member")
@Tag(
        name = "Member Application",
        description = "Member 지원서 관련 API"
)
public class MemberApplicationController {
    private final MemberApplicationService memberApplicationService;

    @PutMapping("/draft")
    @Operation(summary = "Member 지원서 임시저장")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "임시저장 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, APPLICATION_ALREADY_SUBMITTED, INVALID_INTERVIEW_TIME_SLOT, APPLICATION_PERIOD_CLOSED)"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> saveMemberDraft(@RequestBody SaveMemberDraftRequest memberDraft,
        @AuthUser CurrentUser currentUser) {
        memberApplicationService.saveMemberDraft(currentUser.id(), memberDraft);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @PostMapping("/submit")
    @Operation(summary = "Member 지원서 제출")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "제출 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (INVALID_INPUT_VALUE, INVALID_INTERVIEW_TIME_SLOT, APPLICATION_PERIOD_CLOSED)"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 (INTERVIEW_TIME_SLOT_NOT_FOUND)")
    })
    public ResponseEntity<BaseResponse<?>> submitMemberApplication(@Valid @RequestBody SubmitMemberApplicationRequest memberApplication,
        @AuthUser CurrentUser currentUser) {
        memberApplicationService.submitMemberApplication(currentUser.id(), memberApplication);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @GetMapping("/interview-time")
    @Operation(summary = "Member 면접시간대 조회")
    public ResponseEntity<BaseResponse<?>> getMemberInterviewTimeSlots() {
        List<MemberInterviewTimeSlotResponse> interviewTimeSlots = memberApplicationService.getMemberInterviewTimeSlots();
        return ResponseEntity.ok(BaseResponse.success(interviewTimeSlots));
    }

    @GetMapping
    @Operation(summary = "기존에 작성하던 / 제출한 Member 지원서 내용 조회",
            description = "작성한 지원서가 없는 경우 data는 null로 반환됩니다.")
    public ResponseEntity<BaseResponse<?>> getMemberApplication(@AuthUser CurrentUser currentUser) {
        MemberApplicationResponse memberApplication = memberApplicationService.getMemberApplication(currentUser.id());
        return ResponseEntity.ok(BaseResponse.success(memberApplication));
    }

}
