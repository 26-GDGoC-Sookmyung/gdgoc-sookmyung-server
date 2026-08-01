package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.entity.SheetSyncStatus;
import com.gdgoc.website.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByUserAndApplicationType(User user, ApplicationType applicationType);
    @Query("""
    select a.id
    from Application a
    where a.applicationStatus = :applicationStatus
      and a.sheetSyncStatus = :sheetSyncStatus
    order by a.id
""")
    List<Long> findIdsByApplicationStatusAndSheetSyncStatus(
            @Param("applicationStatus") ApplicationStatus applicationStatus,
            @Param("sheetSyncStatus") SheetSyncStatus sheetSyncStatus,
            Pageable pageable
    );
    @Query("""
    select a
    from Application a
    join fetch a.user
    where a.id = :applicationId
""")
    Optional<Application> findByIdWithUser(@Param("applicationId") Long applicationId);
}
