package com.gdgoc.website.user.controller;

import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.global.security.AuthUser;
import com.gdgoc.website.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "회원 정보 관련 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<CurrentUser>> getMyProfile(
            @AuthUser CurrentUser currentUser
    ) {
        return ResponseEntity.ok(
                BaseResponse.success("내 정보를 성공적으로 조회했습니다.", currentUser)
        );
    }
}