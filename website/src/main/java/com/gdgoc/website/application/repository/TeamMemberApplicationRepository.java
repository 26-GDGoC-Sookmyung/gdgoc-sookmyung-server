package com.gdgoc.website.application.repository;

import com.gdgoc.website.application.entity.TeamMemberApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamMemberApplicationRepository extends JpaRepository<TeamMemberApplication, Long> {
    @Query("""
    select distinct t
    from TeamMemberApplication t
    left join fetch t.roles
    where t.id = :applicationId
""")
    Optional<TeamMemberApplication> findByIdWithRoles(@Param("applicationId") Long applicationId);
}
