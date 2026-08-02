package com.gdgoc.website.application.service;

import com.gdgoc.website.application.dto.MemberApplicationResponse;
import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.entity.MemberApplication;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.MemberApplicationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberApplicationRepository memberApplicationRepository;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;

    @Transactional(readOnly = true)
    public MemberApplicationResponse getMemberApplication(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "해당 id의 사용자를 찾을 수 없습니다."));

        Application application = applicationRepository
                .findByUserAndApplicationType(user, ApplicationType.MEMBER)
                .orElse(null);

        if (application == null) {
            return null;
        }

        Long applicationId = application.getId();

        MemberApplication memberApplication = findMemberApplicationOrThrow(applicationId);

        List<Long> interviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

        return MemberApplicationResponse.of(
                application,
                memberApplication,
                interviewTimeSlotIds
        );
    }

    private MemberApplication findMemberApplicationOrThrow(Long applicationId) {
        return memberApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Application(id={})은 존재하지만 MemberApplication이 존재하지 않습니다.", applicationId);
                    return new BaseException(ErrorCode.APPLICATION_DETAIL_NOT_FOUND);
                });
    }

}
