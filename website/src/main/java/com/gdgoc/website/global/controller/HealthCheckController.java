package com.gdgoc.website.global.controller;

import com.gdgoc.website.global.response.BaseResponse;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Health Check", description = "서버 및 DB 상태 확인 API")
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;

    @Operation(summary = "서버 생존 확인 (스케줄러용)", description = "Render 서버의 Sleep을 방지하기 위한 가벼운 핑 용도입니다. DB를 깨우지 않습니다.")
    @GetMapping("/server")
    public ResponseEntity<BaseResponse<Void>> checkServer() {
        return ResponseEntity.ok(
            BaseResponse.success("Render 서버가 정상적으로 깨어있습니다.",null)
        );
    }

    @Operation(summary = "서버 및 DB 생존 확인 (프론트용)", description = "첫 접속 시 Neon DB를 절전 모드에서 깨우기 위한 용도입니다.")
    @GetMapping("/db")
    public ResponseEntity<BaseResponse<Void>> checkDb() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity.ok(
                BaseResponse.success("Render 서버와 Neon DB가 정상적으로 연동 및 활성화되었습니다.",null)
            );
        } catch (Exception e) {
            log.error("DB Wake-up 실패: {}", e.getMessage());
            throw new BaseException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "DB 활성화에 실패했습니다."
            );
        }
    }
}