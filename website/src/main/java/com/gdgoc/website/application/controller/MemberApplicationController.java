package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.MemberApplicationResponse;
import com.gdgoc.website.application.service.MemberApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.global.security.AuthUser;
import com.gdgoc.website.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    @Operation(summary = "기존에 작성하던 / 제출한 Member 지원서 내용 조회",
            description = "작성한 지원서가 없는 경우 data는 null로 반환됩니다.")
    public ResponseEntity<BaseResponse<?>> getMemberApplication(@AuthUser CurrentUser currentUser) {
        MemberApplicationResponse memberApplication = memberApplicationService.getMemberApplication(currentUser.id());
        return ResponseEntity.ok(BaseResponse.success(memberApplication));
    }

}
