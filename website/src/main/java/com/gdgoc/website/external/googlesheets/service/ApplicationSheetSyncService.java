package com.gdgoc.website.external.googlesheets.service;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.MemberApplicationRepository;
import com.gdgoc.website.application.service.TeamMemberApplicationService;
import com.gdgoc.website.external.googlesheets.mapper.MemberApplicationSheetMapper;
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
    private final MemberApplicationRepository memberApplicationRepository;
    private final MemberApplicationSheetMapper memberApplicationSheetMapper;

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

                List<Object> row = teamMemberApplicationSheetMapper.toRow(
                        application,
                        teamMemberApplication,
                        selectedInterviewTimeSlotIds
                );

                googleSheetsClient.appendTeamMemberApplication(applicationId, row);
            } else if (application.getApplicationType() == ApplicationType.MEMBER) {
                MemberApplication memberApplication = findMemberApplicationOrThrow(applicationId);
                List<Long> selectedInterviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

                List<Object> row = memberApplicationSheetMapper.toRow(
                        application,
                        memberApplication,
                        selectedInterviewTimeSlotIds
                );

                googleSheetsClient.appendMemberApplication(applicationId, row);
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

    private MemberApplication findMemberApplicationOrThrow(Long applicationId) {
        return memberApplicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Application(id={})은 존재하지만 MemberApplication이 존재하지 않습니다.", applicationId);
                    return new BaseException(ErrorCode.APPLICATION_DETAIL_NOT_FOUND);
                });
    }

}
