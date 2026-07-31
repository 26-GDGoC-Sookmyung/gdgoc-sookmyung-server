package com.gdgoc.website.external.googlesheets.mapper;

import com.gdgoc.website.application.entity.*;
import com.gdgoc.website.application.repository.InterviewTimeSlotRepository;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.gdgoc.website.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@RequiredArgsConstructor
public class MemberApplicationSheetMapper {

    private static final int MEMBER_SLOT_COUNT = 28;

    private final InterviewTimeSlotRepository interviewTimeSlotRepository;

    public List<Object> toRow(
            Application application,
            MemberApplication memberApplication,
            List<Long> selectedInterviewTimeSlotIds
    ) {
        User user = application.getUser();

        List<Long> interviewTimeSlotIds = interviewTimeSlotRepository
                .findAllByApplicationTypeOrderById(ApplicationType.MEMBER)
                .stream()
                .map(InterviewTimeSlot::getId).toList();

        validateInterviewTimeSlotCount(interviewTimeSlotIds);

        Set<Long> selectedTimeSlotIdSet = selectedInterviewTimeSlotIds == null
                ? Set.of()
                : new HashSet<>(selectedInterviewTimeSlotIds);

        List<Object> row = new ArrayList<>(17 + MEMBER_SLOT_COUNT);

        row.add(application.getId());
        row.add(user.getName());
        row.add(application.getStudentNumber());
        row.add(application.getPhoneNumber());
        row.add(user.getEmail());
        row.add(application.getMajor());
        row.add(application.getCompletedSemester());
        row.add(Boolean.TRUE.equals(application.getOnLeave()) ? "O" : "X");

        row.add(memberApplication.getIntroduction());
        row.add(memberApplication.getMotivation());
        row.add(memberApplication.getGoal());

        row.add(memberApplication.getPortfolioUrl());
        row.add(memberApplication.getExperience());
        row.add(memberApplication.getProject());
        row.add(memberApplication.getStudyInterest());
        row.add(memberApplication.getProjectIdea());
        row.add(memberApplication.getTechStack());

        interviewTimeSlotIds.stream()
                .map(selectedTimeSlotIdSet::contains)
                .forEach(row::add);

        return row;
    }

    private void validateInterviewTimeSlotCount(List<Long> interviewTimeSlotIds) {
        if (interviewTimeSlotIds.size() != MEMBER_SLOT_COUNT) {
            throw new BaseException(
                    ErrorCode.INVALID_INTERVIEW_TIME_SLOT_COUNT,
                    "MEMBER 면접 시간대 개수가 올바르지 않습니다. expected: " + MEMBER_SLOT_COUNT
                            + ", actual: " + interviewTimeSlotIds.size()
            );
        }
    }

}
