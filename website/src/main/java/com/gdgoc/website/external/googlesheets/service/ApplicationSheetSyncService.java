package com.gdgoc.website.external.googlesheets.service;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.service.TeamMemberApplicationService;
import com.gdgoc.website.external.googlesheets.mapper.TeamMemberApplicationSheetMapper;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationSheetSyncService {

    private final ApplicationRepository applicationRepository;
    private final TeamMemberApplicationSheetMapper teamMemberApplicationSheetMapper;
    private final GoogleSheetsClient googleSheetsClient;
    private final TeamMemberApplicationService teamMemberApplicationService;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;

    @Transactional
    public void syncApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Google Sheets 동기화 대상 Application이 존재하지 않습니다. applicationId: {}", applicationId);
                    return new BaseException(ErrorCode.APPLICATION_INTERNAL_NOT_FOUND);
                });

        try {
            if (application.getApplicationType() == ApplicationType.TEAM_MEMBER) {
                TeamMemberApplication teamMemberApplication = teamMemberApplicationService.findTeamMemberApplicationOrThrow(applicationId);
                List<Long> selectedInterviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

                // 구글 시트 행으로 변환
                List<Object> row = teamMemberApplicationSheetMapper.toRow(
                        application,
                        teamMemberApplication,
                        selectedInterviewTimeSlotIds
                );

                // Team Member 구글 시트에 추가
                googleSheetsClient.appendTeamMemberApplication(row);
            }

            application.updateSheetSyncStatus(SheetSyncStatus.SUCCESS);
        } catch (IOException | BaseException e) {
            application.updateSheetSyncStatus(SheetSyncStatus.FAILED);

            log.error(
                    "Google Sheets 지원서 동기화 실패 - applicationId: {}",
                    applicationId,
                    e
            );
        }
    }

}
