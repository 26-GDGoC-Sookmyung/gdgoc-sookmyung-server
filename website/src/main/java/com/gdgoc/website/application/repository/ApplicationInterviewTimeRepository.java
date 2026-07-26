package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationInterviewTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationInterviewTimeRepository extends JpaRepository<ApplicationInterviewTime, Long> {
    void deleteAllByApplication(Application application);
    @Query("""
        select ait.interviewTimeSlot.id
        from ApplicationInterviewTime ait
        where ait.application.id = :applicationId
        """)
    List<Long> findInterviewTimeSlotIdsByApplicationId(Long applicationId);
}
