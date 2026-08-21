package com.gdgoc.website.external.googlesheets.mapper;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.InterviewTimeSlotRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TeamMemberApplicationSheetMapper {

    private static final int TEAM_MEMBER_SLOT_COUNT = 18;

    private final InterviewTimeSlotRepository interviewTimeSlotRepository;

    public List<Object> toRow(
            Application application,
            TeamMemberApplication teamMemberApplication,
            List<Long> selectedInterviewTimeSlotIds
    ) {
        Set<TeamMemberRole> roles = teamMemberApplication.getRoles();
        User user = application.getUser();

        List<Long> interviewTimeSlotIds = interviewTimeSlotRepository
                .findAllByApplicationTypeOrderById(ApplicationType.TEAM_MEMBER)
                .stream()
                .map(InterviewTimeSlot::getId).toList();

        validateInterviewTimeSlotCount(interviewTimeSlotIds);

        return Arrays.asList(
                application.getId(),
                user.getName(),
                application.getStudentNumber(),
                application.getPhoneNumber(),
                user.getEmail(),
                application.getMajor(),
                application.getCompletedSemester(),
                Boolean.TRUE.equals(application.getOnLeave()) ? "O" : "X",

                teamMemberApplication.getIntroduction(),
                teamMemberApplication.getMotivation(),
                teamMemberApplication.getCommunityPerspective(),
                teamMemberApplication.getGoal(),

                teamMemberApplication.getPortfolioUrl(),
                teamMemberApplication.getProject(),
                teamMemberApplication.getLeadershipExperience(),
                teamMemberApplication.getTechStack(),

                teamMemberApplication.getStudyField(),
                roles.contains(TeamMemberRole.STUDY_LEAD),
                roles.contains(TeamMemberRole.MANAGEMENT),
                roles.contains(TeamMemberRole.PROJECT_MENTOR),
                teamMemberApplication.getEventIdea(),

                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(0)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(1)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(2)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(3)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(4)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(5)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(6)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(7)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(8)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(9)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(10)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(11)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(12)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(13)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(14)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(15)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(16)),
                selectedInterviewTimeSlotIds.contains(interviewTimeSlotIds.get(17))
            );
    }

    private void validateInterviewTimeSlotCount(List<Long> interviewTimeSlotIds) {
        if (interviewTimeSlotIds.size() != TEAM_MEMBER_SLOT_COUNT) {
            throw new BaseException(
                    ErrorCode.INVALID_INTERVIEW_TIME_SLOT_COUNT,
                    "TEAM_MEMBER 면접 시간대 개수가 올바르지 않습니다. expected: " + TEAM_MEMBER_SLOT_COUNT
                            + ", actual: " + interviewTimeSlotIds.size()
            );
        }
    }

}
