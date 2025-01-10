package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CourseDetail extends CourseEnrollment {

  public CourseDetail() {
    super();
  }

  public static CourseDetail from(Course course, CourseMemberTracking courseMemberTracking) {
    return CourseDetail.builder()
        .id(course.getId())
        .name(course.getName())
        .reopenedBasedOn(course.getReopenedBasedOn().toHexString())
        .slug(course.getSlug())
        .limitStudent(course.getLimitStudent())
        .description(course.getDescription())
        .teacherName(course.getTeacherName())
        .startDate(course.getStartDate())
        .endDate(course.getEndDate())
        .createdAt(course.getCreatedAt())
        .updatedAt(course.getUpdatedAt())
        .status(courseMemberTracking.getStatus())
        .completionProgress(courseMemberTracking.getCompletionProgress())
        .note(courseMemberTracking.getNote())
        .forciblyFinishedAt(courseMemberTracking.getForciblyFinishedAt())
        .cancelledAt(courseMemberTracking.getCancelledAt())
        .expiredAt(courseMemberTracking.getExpiredAt())
        .enrolledAt(courseMemberTracking.getEnrolledAt())
        .certificateUrl(courseMemberTracking.getCertificateUrl())
        .build();
  }
}
