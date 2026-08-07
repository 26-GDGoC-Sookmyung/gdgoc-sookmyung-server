package com.gdgoc.website.external.googlesheets.service;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.MemberApplicationRepository;
import com.gdgoc.website.application.repository.TeamMemberApplicationRepository;
import com.gdgoc.website.application.service.ApplicationSheetStatusService;
import com.gdgoc.website.auth.service.MailService;
import com.gdgoc.website.external.googlesheets.mapper.MemberApplicationSheetMapper;
import com.gdgoc.website.external.googlesheets.mapper.TeamMemberApplicationSheetMapper;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationSheetSyncService {

    @Value("${mail.admin-email}")
    private String adminEmail;

    private final ApplicationRepository applicationRepository;
    private final TeamMemberApplicationSheetMapper teamMemberApplicationSheetMapper;
    private final GoogleSheetsClient googleSheetsClient;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;
    private final MemberApplicationRepository memberApplicationRepository;
    private final MemberApplicationSheetMapper memberApplicationSheetMapper;
    private final ApplicationSheetStatusService applicationSheetStatusService;
    private final TeamMemberApplicationRepository teamMemberApplicationRepository;
    private final MailService mailService;

    public void syncApplication(Long applicationId) {
        ApplicationType applicationType = null;

        try {
            Application application = applicationRepository.findByIdWithUser(applicationId)
                    .orElseThrow(() -> {
                        log.error("Google Sheets 동기화 대상 Application이 존재하지 않습니다. applicationId: {}", applicationId);
                        return new BaseException(ErrorCode.APPLICATION_INTERNAL_NOT_FOUND);
                    });

            applicationType = application.getApplicationType();

            switch (applicationType) {
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

            try {
                sendSheetSyncFailureEmail(
                        applicationId,
                        applicationType,
                        e.getMessage()
                );
            } catch (Exception mailException) {
                log.error(
                        "Google Sheets 동기화 실패 알림 메일 전송 실패 - applicationId: {}",
                        applicationId,
                        mailException
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

    /** 구글 시트 동기화 실패 시 메일 전송 */
    private void sendSheetSyncFailureEmail(
            Long applicationId,
            ApplicationType applicationType,
            String errorMessage
    ) {
        String safeErrorMessage =
                errorMessage != null
                        ? HtmlUtils.htmlEscape(errorMessage)
                        : "오류 메시지 없음";

        String subject = "[GDGoC] 구글 시트 동기화 실패";
        String htmlContent = """
        <h2>구글 시트 동기화 실패</h2>
        <p><strong>지원서 ID:</strong> %d</p>
        <p><strong>지원 유형:</strong> %s</p>
        <p><strong>오류:</strong> %s</p>
        <p>관리자 확인 및 재동기화가 필요합니다.</p>
        """.formatted(applicationId, applicationType, safeErrorMessage);

        mailService.sendEmail(adminEmail, subject, htmlContent);
    }

}
