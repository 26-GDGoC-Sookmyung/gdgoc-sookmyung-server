package com.gdgoc.website.external.googlesheets.scheduler;

import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.SheetSyncStatus;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.external.googlesheets.service.ApplicationSheetSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSheetSyncScheduler {

    private final ApplicationSheetSyncService applicationSheetSyncService;
    private final ApplicationRepository applicationRepository;

    @Scheduled(fixedDelay = 30000) // 30초
    public void syncPendingApplications() {
        List<Long> applicationIds =
                applicationRepository.findIdsByApplicationStatusAndSheetSyncStatus(
                        ApplicationStatus.SUBMITTED,
                        SheetSyncStatus.PENDING,
                        PageRequest.of(0, 10)
                );

        for (Long applicationId : applicationIds) {
            applicationSheetSyncService.syncApplication(applicationId);
        }
    }

}
