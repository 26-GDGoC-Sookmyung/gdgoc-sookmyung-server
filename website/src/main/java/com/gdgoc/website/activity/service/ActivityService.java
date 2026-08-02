package com.gdgoc.website.activity.service;

import com.gdgoc.website.activity.dto.ActivityByQuarterResponse;
import com.gdgoc.website.activity.entity.Activity;
import com.gdgoc.website.activity.entity.Quarter;
import com.gdgoc.website.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private static final int CURRENT_GENERATION = 6;

    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityByQuarterResponse> getActivities() {
        List<Activity> activities = activityRepository.findAllByGenerationOrderByQuarterAscIdAsc(CURRENT_GENERATION);

        return Arrays.stream(Quarter.values())
                .map(quarter -> toQuarterResponse(quarter, activities))
                .toList();
    }

    private ActivityByQuarterResponse toQuarterResponse(Quarter quarter,
                                                        List<Activity> activities) {
        // quarter에 해당하는 활동만 필터링
        List<ActivityByQuarterResponse.ActivityResponse> activityResponses =
                activities.stream()
                        .filter(activity -> activity.getQuarter() == quarter)
                        .map(ActivityByQuarterResponse.ActivityResponse::from)
                        .toList();

        return new ActivityByQuarterResponse(
                quarter.getLabel(),
                activityResponses
        );
    }

}
