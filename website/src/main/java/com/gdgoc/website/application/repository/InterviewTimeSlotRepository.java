package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.entity.InterviewTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTimeSlotRepository extends JpaRepository<InterviewTimeSlot, Long> {
    List<InterviewTimeSlot> findAllByApplicationTypeOrderById(ApplicationType applicationType);
}
