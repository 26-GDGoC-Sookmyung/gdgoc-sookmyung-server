package com.gdgoc.website.application.entity;

import com.gdgoc.website.global.entity.BaseTimeEntity;
import com.gdgoc.website.user.entity.User;
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
                        columnNames = {"user_id", "application_type"}
                )
        }
)
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationType applicationType;

    private String studentNumber;

    private String phoneNumber;

    private String major;

    // 2026-1 이수학기
    private Integer completedSemester;

    // 2026-2 휴학여부
    private Boolean onLeave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus applicationStatus;

    // 구글 시트 반영 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SheetSyncStatus sheetSyncStatus;

    public Application(
            User user,
            ApplicationType applicationType,
            String studentNumber,
            String phoneNumber,
            String major,
            Integer completedSemester,
            Boolean onLeave
    ) {
        this.user = user;
        this.applicationType = applicationType;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.completedSemester = completedSemester;
        this.onLeave = onLeave;
        this.applicationStatus = ApplicationStatus.DRAFT;
        this.sheetSyncStatus = SheetSyncStatus.NONE;
    }

    public void update(
            String studentNumber,
            String phoneNumber,
            String major,
            Integer completedSemester,
            Boolean onLeave
    ) {
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.major = major;
        this.completedSemester = completedSemester;
        this.onLeave = onLeave;
    }

    public void submit() {
        this.applicationStatus = ApplicationStatus.SUBMITTED;
        this.sheetSyncStatus = SheetSyncStatus.PENDING;
    }

    public void updateSheetSyncStatus(SheetSyncStatus sheetSyncStatus) {
        this.sheetSyncStatus = sheetSyncStatus;
    }

}
