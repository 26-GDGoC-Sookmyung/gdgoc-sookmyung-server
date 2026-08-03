package com.gdgoc.website.application.service;

import com.gdgoc.website.application.dto.SaveTeamMemberDraftRequest;
import com.gdgoc.website.application.dto.SubmitTeamMemberApplicationRequest;
import com.gdgoc.website.application.dto.TeamMemberApplicationResponse;
import com.gdgoc.website.application.dto.TeamMemberInterviewTimeSlotResponse;
import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.ApplicationInterviewTimeRepository;
import com.gdgoc.website.application.repository.ApplicationRepository;
import com.gdgoc.website.application.repository.InterviewTimeSlotRepository;
import com.gdgoc.website.application.repository.TeamMemberApplicationRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import com.gdgoc.website.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamMemberApplicationService {

    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TeamMemberApplicationRepository teamMemberApplicationRepository;
    private final InterviewTimeSlotRepository interviewTimeSlotRepository;
    private final ApplicationInterviewTimeRepository applicationInterviewTimeRepository;

    @Transactional
    public void saveTeamMemberDraft(Long userId, SaveTeamMemberDraftRequest request) {
        UserApplicationContext userAndApplication = getUserAndApplication(userId);
        Application application = userAndApplication.application();

        if (application != null && application.getApplicationStatus() == ApplicationStatus.SUBMITTED) {
            throw new BaseException(ErrorCode.APPLICATION_ALREADY_SUBMITTED);
        }
        
         saveOrUpdateTeamMemberApplication(userAndApplication.user(), application, request);
    }

    @Transactional
    public void submitTeamMemberApplication(Long userId, SubmitTeamMemberApplicationRequest request) {
        UserApplicationContext userAndApplication = getUserAndApplication(userId);

        Application application = saveOrUpdateTeamMemberApplication(userAndApplication.user(), userAndApplication.application(), toSaveTeamMemberDraftRequest(request));
        application.submit();
    }

    private SaveTeamMemberDraftRequest toSaveTeamMemberDraftRequest(SubmitTeamMemberApplicationRequest request) {
        return new SaveTeamMemberDraftRequest(
                request.studentNumber(),
                request.phoneNumber(),
                request.major(),
                request.completedSemester(),
                request.onLeave(),
                request.introduction(),
                request.motivation(),
                request.communityPerspective(),
                request.goal(),
                request.portfolioUrl(),
                request.project(),
                request.leadershipExperience(),
                request.techStack(),
                request.studyField(),
                request.roles(),
                request.eventIdea(),
                request.interviewTimeSlotIds()
        );
    }

    /** Team Member Application 생성 및 수정 공통 로직 */
    private Application saveOrUpdateTeamMemberApplication(User user, Application application, SaveTeamMemberDraftRequest request) {
        // 지원 기간인지 확인
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE_ID);

        // 테스트를 위해 주석처리
//        if (!ApplicationType.TEAM_MEMBER.isSubmissionOpen(now)) {
//            throw new BaseException(ErrorCode.APPLICATION_PERIOD_CLOSED);
//        }

        if (application == null) {
            // 사용자가 작성한 Team Member 지원서가 없을 경우 새로 생성
            application = new Application(
                    user,
                    ApplicationType.TEAM_MEMBER,
                    request.studentNumber(),
                    request.phoneNumber(),
                    request.major(),
                    request.completedSemester(),
                    request.onLeave()
            );

            applicationRepository.save(application);

            TeamMemberApplication teamMemberApplication = new TeamMemberApplication(
                    application,
                    request.introduction(),
                    request.motivation(),
                    request.communityPerspective(),
                    request.goal(),
                    request.portfolioUrl(),
                    request.project(),
                    request.leadershipExperience(),
                    request.techStack(),
                    request.studyField(),
                    request.roles(),
                    request.eventIdea()
            );

            teamMemberApplicationRepository.save(teamMemberApplication);
        } else {
            // 있을 경우 업데이트
            application.update(
                    request.studentNumber(),
                    request.phoneNumber(),
                    request.major(),
                    request.completedSemester(),
                    request.onLeave()
            );

            TeamMemberApplication teamMemberApplication = findTeamMemberApplicationOrThrow(application.getId());

            teamMemberApplication.update(
                    request.introduction(),
                    request.motivation(),
                    request.communityPerspective(),
                    request.goal(),
                    request.portfolioUrl(),
                    request.project(),
                    request.leadershipExperience(),
                    request.techStack(),
                    request.studyField(),
                    request.roles(),
                    request.eventIdea()
            );
        }
        replaceApplicationInterviewTimes(application, request.interviewTimeSlotIds());

        return application;
    }

    private UserApplicationContext getUserAndApplication(Long userId) {
        // 현재 로그인한 사용자 조회
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "해당 id의 사용자를 찾을 수 없습니다."));

        // 해당 사용자가 작성한 Team Member 지원서가 있는지 확인
        Application application = applicationRepository
                .findByUserAndApplicationType(user, ApplicationType.TEAM_MEMBER)
                .orElse(null);
        
        return new UserApplicationContext(user, application);
    }

    public TeamMemberApplication findTeamMemberApplicationOrThrow(Long applicationId) {
        return teamMemberApplicationRepository
                .findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Application(id={})은 존재하지만 TeamMemberApplication이 존재하지 않습니다.", applicationId);
                    return new BaseException(ErrorCode.APPLICATION_DETAIL_NOT_FOUND);
                });
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

    public List<TeamMemberInterviewTimeSlotResponse> getTeamMemberInterviewTimeSlots() {
        List<InterviewTimeSlot> timeSlots = interviewTimeSlotRepository.findAllByApplicationTypeOrderById(ApplicationType.TEAM_MEMBER);
        return timeSlots.stream().map(this::toTeamMemberInterviewTimeSlotResponse).toList();
    }

    private TeamMemberInterviewTimeSlotResponse toTeamMemberInterviewTimeSlotResponse(InterviewTimeSlot interviewTimeSlot) {
        return new TeamMemberInterviewTimeSlotResponse(
                interviewTimeSlot.getId(),
                interviewTimeSlot.getTimeSlot()
        );
    }

    @Transactional(readOnly = true)
    public TeamMemberApplicationResponse getTeamMemberApplication(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND, "해당 id의 사용자를 찾을 수 없습니다."));

        Application application = applicationRepository
                .findByUserAndApplicationType(user, ApplicationType.TEAM_MEMBER)
                .orElse(null);

        if (application == null) {
            return null;
        }

        Long applicationId = application.getId();

        TeamMemberApplication teamMemberApplication = findTeamMemberApplicationOrThrow(applicationId);

        List<Long> interviewTimeSlotIds = applicationInterviewTimeRepository.findInterviewTimeSlotIdsByApplicationId(applicationId);

        return TeamMemberApplicationResponse.of(
                application,
                teamMemberApplication,
                interviewTimeSlotIds
        );
    }

    private record UserApplicationContext(
            User user,
            Application application
    ){}

}
