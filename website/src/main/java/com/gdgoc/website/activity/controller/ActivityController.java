package com.gdgoc.website.activity.controller;

import com.gdgoc.website.activity.dto.ActivityByQuarterResponse;
import com.gdgoc.website.activity.service.ActivityService;
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
@RequestMapping("/api/activities")
@Tag(
        name = "Activities",
        description = "활동 관련 API"
)
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "분기별 활동 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    public ResponseEntity<BaseResponse<?>> getActivities() {
        List<ActivityByQuarterResponse> activities = activityService.getActivities();
        return ResponseEntity.ok(BaseResponse.success(activities));
    }

}
