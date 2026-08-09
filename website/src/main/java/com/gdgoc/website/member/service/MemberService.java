package com.gdgoc.website.member.service;

import com.gdgoc.website.member.dto.MemberResponse;
import com.gdgoc.website.member.entity.Member;
import com.gdgoc.website.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberResponse> getMembersByGeneration(int generation) {
        List<Member> members = memberRepository.findAllByGenerationAndIsVisibleTrue(generation);
        return members.stream()
                .sorted(Comparator.comparingInt((Member m) -> m.getRole().getPriority())
                                  .thenComparing(Member::getId))
                .map(MemberResponse::from)
                .toList();
    }
}