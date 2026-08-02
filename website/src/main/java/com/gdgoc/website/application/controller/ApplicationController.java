package com.gdgoc.website.application.controller;

import com.gdgoc.website.application.dto.ApplicationResponse;
import com.gdgoc.website.application.service.ApplicationService;
import com.gdgoc.website.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
@Tag(
        name = "Application",
        description = "지원 현황 관련 API"
)
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    @Operation(summary = "지원 현황 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<BaseResponse<?>> getApplications() {
        List<ApplicationResponse> applications = applicationService.getApplications(1L);
        return ResponseEntity.ok(BaseResponse.success(applications));
    }

}
