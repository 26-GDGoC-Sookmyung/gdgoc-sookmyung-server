package com.gdgoc.website.global.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KeepAliveScheduler {

    private final RestClient restClient = RestClient.create();

    @Value("${app.public-url:http://localhost:8080}")
    private String publicUrl;

    // 14분(840000ms)마다 실행
    @Scheduled(fixedRate = 840000)
    public void pingToKeepAlive() {
        String healthCheckUrl = publicUrl + "/api/health/server";
        
        try {
            log.info("[KeepAlive] Render 서버 Sleep 방지 핑 전송 시작");

            String response = restClient.get()
                    .uri(healthCheckUrl)
                    .retrieve()
                    .body(String.class);
                    
            log.info("[KeepAlive] 응답 성공: {}", response);
        } catch (Exception e) {
            log.error("[KeepAlive] 핑 전송 실패: {}", e.getMessage());
        }
    }
}