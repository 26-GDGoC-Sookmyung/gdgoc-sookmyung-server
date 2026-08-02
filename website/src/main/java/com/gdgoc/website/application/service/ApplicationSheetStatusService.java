package com.gdgoc.website.application.service;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.SheetSyncStatus;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationSheetStatusService {

    private final ApplicationRepository applicationRepository;

    @Transactional
    public void markSuccess(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ErrorCode.APPLICATION_INTERNAL_NOT_FOUND, "해당 id의 Application을 찾을 수 없습니다. id = " + applicationId));

        application.updateSheetSyncStatus(SheetSyncStatus.SUCCESS);
    }

    @Transactional
    public void markFailed(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ErrorCode.APPLICATION_INTERNAL_NOT_FOUND, "해당 id의 Application을 찾을 수 없습니다. id = " + applicationId));

        application.updateSheetSyncStatus(SheetSyncStatus.FAILED);
    }

}
