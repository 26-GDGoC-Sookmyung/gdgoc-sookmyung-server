package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationInterviewTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationInterviewTimeRepository extends JpaRepository<ApplicationInterviewTime, Long> {
    void deleteAllByApplication(Application application);
}
