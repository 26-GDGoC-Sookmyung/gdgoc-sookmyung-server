package com.gdgoc.website.application.service;

import com.gdgoc.website.application.dto.MemberApplicationResponse;
import com.gdgoc.website.application.dto.MemberInterviewTimeSlotResponse;
import com.gdgoc.website.application.dto.SaveMemberDraftRequest;
import com.gdgoc.website.application.dto.SubmitMemberApplicationRequest;
import com.gdgoc.website.application.entity.Application;
import com.gdgoc.website.application.entity.ApplicationInterviewTime;
import com.gdgoc.website.application.entity.ApplicationStatus;
import com.gdgoc.website.application.entity.ApplicationType;
import com.gdgoc.website.application.entity.InterviewTimeSlot;
import com.gdgoc.website.application.entity.MemberApplication;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.InterviewTimeSlotRepository;
import com.gdgoc.website.application.repository.MemberApplicationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberApplicationService {

    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberApplicationRepository memberApplicationRepository;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;
    private final InterviewTimeSlotRepository interviewTimeSlotRepository;

    @Transactional
    public void saveMemberDraft(Long userId, SaveMemberDraftRequest request) {
        UserApplicationContext userAndApplication = getUserAndApplication(userId);
        Application application = userAndApplication.application();

        if (application != null && application.getApplicationStatus() == ApplicationStatus.SUBMITTED) {
            throw new BaseException(ErrorCode.APPLICATION_ALREADY_SUBMITTED);
        }

        saveOrUpdateMemberApplication(userAndApplication.user(), application, request);
    }

    @Transactional
    public void submitMemberApplication(Long userId, SubmitMemberApplicationRequest request) {
        UserApplicationContext userAndApplication = getUserAndApplication(userId);

        Application application = saveOrUpdateMemberApplication(userAndApplication.user(), userAndApplication.application(), toSaveMemberDraftRequest(request));
        application.submit();
    }

    private SaveMemberDraftRequest toSaveMemberDraftRequest(SubmitMemberApplicationRequest request) {
        return new SaveMemberDraftRequest(
            request.studentNumber(),
            request.phoneNumber(),
            request.major(),
            request.completedSemester(),
            request.onLeave(),
            request.introduction(),
            request.motivation(),
            request.goal(),
            request.portfolioUrl(),
            request.experience(),
            request.project(),
            request.studyInterest(),
            request.projectIdea(),
            request.techStack(),
            request.interviewTimeSlotIds()
        );
    }

    /** Member Application 생성 및 수정 공통 로직 */
    private Application saveOrUpdateMemberApplication(User user, Application application, SaveMemberDraftRequest request) {
        // 지원 기간인지 확인
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE_ID);

        if (!ApplicationType.MEMBER.isSubmissionOpen(now)) {
            throw new BaseException(ErrorCode.APPLICATION_PERIOD_CLOSED);
        }

        if (application == null) {
            application = new Application(
                user,
                ApplicationType.MEMBER,
                request.studentNumber(),
                request.phoneNumber(),
                request.major(),
                request.completedSemester(),
                request.onLeave()
            );

            applicationRepository.save(application);

            MemberApplication memberApplication = new MemberApplication(
                application, request.introduction(), request.motivation(), request.goal(),
                request.portfolioUrl(), request.experience(), request.project(),
                request.studyInterest(), request.projectIdea(), request.techStack()
            );

            memberApplicationRepository.save(memberApplication);
        } else {
            // 있을 경우 업데이트
            application.update(
                request.studentNumber(),
                request.phoneNumber(),
                request.major(),
                request.completedSemester(),
                request.onLeave()
            );

            MemberApplication memberApplication = findMemberApplicationOrThrow(application.getId());

            memberApplication.update(
                request.introduction(), request.motivation(), request.goal(),
                request.portfolioUrl(), request.experience(), request.project(),
                request.studyInterest(), request.projectIdea(), request.techStack()
            );
        }
        replaceApplicationInterviewTimes(application, request.interviewTimeSlotIds());

        return application;
    }

    /** 면접 가능 시간을 새 목록으로 교체 */
    private void replaceApplicationInterviewTimes(Application application, List<Long> availableInterviewTimeSlotIds) {
        Set<Long> uniqueTimeSlotIds = availableInterviewTimeSlotIds == null
            ? Set.of()
            : new HashSet<>(availableInterviewTimeSlotIds);

        List<InterviewTimeSlot> availableInterviewTimeSlots = interviewTimeSlotRepository.findAllById(uniqueTimeSlotIds);

        // 1. 존재하지 않는 timeSlotId가 포함되어있는지 확인
        if (availableInterviewTimeSlots.size() != uniqueTimeSlotIds.size()) {
            throw new BaseException(ErrorCode.INTERVIEW_TIME_SLOT_NOT_FOUND, "존재하지 않는 면접 시간대가 포함되어 있습니다.");
        }

        // 2. Application의 지원 유형과 availableInterviewTimeSlots의 지원 유형이 일치하는지 검증
        ApplicationType applicationType = application.getApplicationType();
        boolean hasInvalidApplicationType = availableInterviewTimeSlots.stream()
            .anyMatch(slot -> slot.getApplicationType() != applicationType);

        if (hasInvalidApplicationType) {
            throw new BaseException(ErrorCode.INVALID_INTERVIEW_TIME_SLOT, "지원 유형에 해당하지 않는 면접 시간대가 포함되어 있습니다.");
        }

        List<ApplicationInterviewTime> applicationInterviewTimes = availableInterviewTimeSlots.stream()
            .map(slot -> new ApplicationInterviewTime(application, slot))
            .toList();

        // 3. 전체 삭제 후 새로 저장
        applicationInterviewTimeRepository.deleteAllByApplication(application);
        applicationInterviewTimeRepository.flush();
        applicationInterviewTimeRepository.saveAll(applicationInterviewTimes);
    }

    public List<MemberInterviewTimeSlotResponse> getMemberInterviewTimeSlots() {
        List<InterviewTimeSlot> timeSlots = interviewTimeSlotRepository.findAllByApplicationTypeOrderById(ApplicationType.MEMBER);
        return timeSlots.stream().map(this::toMemberInterviewTimeSlotResponse).toList();
    }

    private MemberInterviewTimeSlotResponse toMemberInterviewTimeSlotResponse(InterviewTimeSlot interviewTimeSlot) {
        return new MemberInterviewTimeSlotResponse(
            interviewTimeSlot.getId(),
            interviewTimeSlot.getTimeSlot()
        );
    }


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

    private MemberApplicationService.UserApplicationContext getUserAndApplication(Long userId) {
        // 현재 로그인한 사용자 조회
        User user = userRepository.findByIdForUpdate(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "해당 id의 사용자를 찾을 수 없습니다."));

        // 해당 사용자가 작성한 Member 지원서가 있는지 확인
        Application application = applicationRepository
            .findByUserAndApplicationType(user, ApplicationType.MEMBER)
            .orElse(null);

        return new MemberApplicationService.UserApplicationContext(user, application);
    }

    private record UserApplicationContext(
        User user,
        Application application
    ){}
}
