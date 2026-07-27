package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.SaveTeamMemberDraftRequest;
import com.gdgoc.website.application.service.TeamMemberApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications/team-member")
public class TeamMemberApplicationController {
    private final TeamMemberApplicationService teamMemberApplicationService;

    // TODO : 추후 로그인 기능 구현 시 @AuthenticationPrincipal 추가

    @PutMapping("/draft")
    public ResponseEntity<BaseResponse<?>> saveTeamMemberDraft(@RequestBody SaveTeamMemberDraftRequest teamMemberDraft) {
        teamMemberApplicationService.saveTeamMemberDraft(1L, teamMemberDraft);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

}
