package com.gdgoc.website.application.service;

import com.gdgoc.website.application.dto.ApplicationResponse;
import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public List<ApplicationResponse> getApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "해당 id의 사용자를 찾을 수 없습니다."));

        List<Application> applications = applicationRepository.findAllByUser(user);

        // TeamMember 지원서가 위로 오게 순서 조정
        applications.sort(
                Comparator.comparingInt(application ->
                        switch (application.getApplicationType()) {
                            case TEAM_MEMBER -> 0;
                            case MEMBER -> 1;
                        }
                )
        );

        return applications.stream()
                .map(application -> ApplicationResponse.of(application, calculateDDay(application.getApplicationType())))
                .toList();
    }

    private String calculateDDay(ApplicationType applicationType) {
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE_ID);
        LocalDateTime endAt = applicationType.getRecruitmentEndAt();

        // 마감 시각과 같거나 이후면 마감
        if (!now.isBefore(endAt)) {
            return "마감";
        }

        // endAt이 다음 날 00:00이므로 실제 마지막 모집 날짜 계싼
        LocalDate lastRecruitmentDate = endAt.minusNanos(1).toLocalDate();

        long days = ChronoUnit.DAYS.between(
                now.toLocalDate(),
                lastRecruitmentDate
        );

        if (days == 0) {
            return "D-Day";
        }

        return "D-" + days;
    }

}
