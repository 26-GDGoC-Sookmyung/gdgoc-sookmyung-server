package com.gdgoc.website.member.controller;

import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.member.dto.MemberResponse;
import com.gdgoc.website.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member API", description = "GDGoC 멤버 소개 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "기수별 멤버 목록 조회", description = "특정 기수의 공개된 멤버 목록을 우선순위에 맞게 정렬하여 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<MemberResponse>>> getMembersByGeneration(
            @Parameter(description = "조회할 기수 (기본값: 6)", example = "6")
            @RequestParam(defaultValue = "6") int generation
    ) {
        List<MemberResponse> members = memberService.getMembersByGeneration(generation);
        
        return ResponseEntity.ok(
                BaseResponse.success(generation + "기 멤버 목록을 성공적으로 조회했습니다.", members)
        );
    }
}