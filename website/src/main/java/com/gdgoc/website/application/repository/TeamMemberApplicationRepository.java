package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.TeamMemberApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberApplicationRepository extends JpaRepository<TeamMemberApplication, Long> {
}
