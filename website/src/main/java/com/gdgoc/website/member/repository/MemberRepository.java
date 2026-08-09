package com.gdgoc.website.member.repository;

import com.gdgoc.website.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByGenerationAndIsVisibleTrue(int generation);
}