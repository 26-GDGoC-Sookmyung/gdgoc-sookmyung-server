package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.entity.SheetSyncStatus;
import com.gdgoc.website.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUserAndApplicationType(User user, ApplicationType applicationType);
    List<Application> findTop10ByApplicationStatusAndSheetSyncStatusInOrderByIdAsc(ApplicationStatus applicationStatus,
                                                                                   List<SheetSyncStatus> sheetSyncStatuses);
}
