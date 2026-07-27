package com.gdgoc.website.application.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"application_id", "interview_time_slot_id"}
                )
        }
)
public class ApplicationInterviewTime {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_time_slot_id", nullable = false)
    private InterviewTimeSlot interviewTimeSlot;

    public ApplicationInterviewTime(
            Application application,
            InterviewTimeSlot interviewTimeSlot
    ) {
        this.application = application;
        this.interviewTimeSlot = interviewTimeSlot;
    }

}
