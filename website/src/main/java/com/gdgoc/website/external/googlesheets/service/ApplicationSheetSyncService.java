package com.gdgoc.website.external.googlesheets.service;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.MemberApplicationRepository;
import com.gdgoc.website.application.repository.TeamMemberApplicationRepository;
import com.gdgoc.website.application.service.ApplicationSheetStatusService;
import com.gdgoc.website.external.googlesheets.mapper.MemberApplicationSheetMapper;
import com.gdgoc.website.external.googlesheets.mapper.TeamMemberApplicationSheetMapper;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationSheetSyncService {

    private final ApplicationRepository applicationRepository;
    private final TeamMemberApplicationSheetMapper teamMemberApplicationSheetMapper;
    private final GoogleSheetsClient googleSheetsClient;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;
    private final MemberApplicationRepository memberApplicationRepository;
    private final MemberApplicationSheetMapper memberApplicationSheetMapper;
    private final ApplicationSheetStatusService applicationSheetStatusService;
    private final TeamMemberApplicationRepository teamMemberApplicationRepository;

    public void syncApplication(Long applicationId) {
        try {
            Application application = applicationRepository.findByIdWithUser(applicationId)
                    .orElseThrow(() -> {
                        log.error("Google Sheets 동기화 대상 Application이 존재하지 않습니다. applicationId: {}", applicationId);
                        return new BaseException(ErrorCode.APPLICATION_INTERNAL_NOT_FOUND);
                    });

            switch (application.getApplicationType()) {
                case TEAM_MEMBER -> syncTeamMemberApplication(application);
                case MEMBER -> syncMemberApplication(application);
                default -> throw new BaseException(ErrorCode.INVALID_APPLICATION_TYPE,
                        "지원하지 않는 Application Type입니다 - " + application.getApplicationType());
            }

            applicationSheetStatusService.markSuccess(applicationId);
        } catch (Exception e) {
            log.error(
                    "Google Sheets 지원서 동기화 실패 - applicationId: {}",
                    applicationId,
                    e
            );

            try {
                applicationSheetStatusService.markFailed(applicationId);
            } catch (Exception statusException) {
                log.error(
                        "Google Sheets 동기화 실패 상태 저장 실패 - applicationId: {}",
                        applicationId,
                        statusException
                );
            }
        }
    }

    private void syncTeamMemberApplication(Application application) throws IOException {
        Long applicationId = application.getId();
        TeamMemberApplication teamMemberApplication = teamMemberApplicationRepository.findByIdWithRoles(applicationId)
                .orElseThrow(() -> new BaseException(ErrorCode.APPLICATION_DETAIL_NOT_FOUND));
        List<Long> selectedInterviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

        List<Object> row = teamMemberApplicationSheetMapper.toRow(
                application,
                teamMemberApplication,
                selectedInterviewTimeSlotIds
        );

        googleSheetsClient.appendTeamMemberApplication(applicationId, row);
    }

    private void syncMemberApplication(Application application) throws IOException {
        Long applicationId = application.getId();
        MemberApplication memberApplication = memberApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ErrorCode.APPLICATION_DETAIL_NOT_FOUND));
        List<Long> selectedInterviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

        List<Object> row = memberApplicationSheetMapper.toRow(
                application,
                memberApplication,
                selectedInterviewTimeSlotIds
        );

        googleSheetsClient.appendMemberApplication(applicationId, row);
    }

}
