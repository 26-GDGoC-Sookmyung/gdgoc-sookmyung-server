package com.gdgoc.website.auth.repository;

import com.gdgoc.website.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);
    Optional<EmailVerification> findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}