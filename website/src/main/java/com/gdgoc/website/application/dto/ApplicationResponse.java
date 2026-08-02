package com.gdgoc.website.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.ApplicationType;

import java.time.LocalDateTime;

public record ApplicationResponse(

        Long id,

        ApplicationType applicationType,

        ApplicationStatus applicationStatus,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime recruitmentStartAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime recruitmentEndAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime lastModifiedAt,

        String dDay

) {

    public static ApplicationResponse of(Application application,
                                           String dDay) {
        ApplicationType applicationType = application.getApplicationType();
        return new ApplicationResponse(
                application.getId(),
                applicationType,
                application.getApplicationStatus(),
                applicationType.getRecruitmentStartAt(),
                applicationType.getRecruitmentEndAt().minusMinutes(1), // 23:59로 표시
                application.getUpdatedAt(),
                dDay
        );
    }

}
