package com.gdgoc.website.activity.dto;

import com.gdgoc.website.activity.entity.Activity;

import java.util.List;

public record ActivityByQuarterResponse(
        String quarter,
        List<ActivityResponse> activities
) {

    public record ActivityResponse(
            String name,
            String description,
            String period,
            String imageUrl
    ) {
        public static ActivityResponse from(Activity activity) {
            return new ActivityResponse(
                    activity.getName(),
                    activity.getDescription(),
                    activity.getPeriod(),
                    activity.getImageUrl()
            );
        }
    }

}
