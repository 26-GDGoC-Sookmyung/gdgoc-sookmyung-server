package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.MemberApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberApplicationRepository extends JpaRepository<MemberApplication, Long> {
}
