package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
@Setter
@SuperBuilder
public class CourseDetail extends CourseEnrollment {

  public CourseDetail() {
    super();
  }

  public static CourseDetail from(
      @NonNull Course course, @Nullable CourseMember courseMemberTracking) {
    var result =
        CourseDetail.builder()
            .id(course.getId())
            .name(course.getName())
            .slug(course.getSlug())
            .limitStudent(course.getLimitStudent())
            .wallpaper(course.getWallpaper())
            .trainingProviderName(course.getTrainingProviderName())
            .trainingProviderLogo(course.getTrainingProviderLogo())
            .certified(course.isCertified())
            .archivedPosition(course.getArchivedPosition())
            .description(course.getDescription())
            .teacherName(course.getTeacherName())
            .isRegistrationEnabled(course.isRegistrationEnabled())
            .startDate(course.getStartDate())
            .endDate(course.getEndDate())
            .createdAt(course.getCreatedAt())
            .updatedAt(course.getUpdatedAt())
            .build();

    if (courseMemberTracking != null) {
      // user is enrolled
      result.setStatus(courseMemberTracking.getStatus());
      result.setCompletionProgress(courseMemberTracking.getCompletionProgress());
      result.setNote(courseMemberTracking.getNote());
      result.setForciblyFinishedAt(courseMemberTracking.getForciblyFinishedAt());
      result.setCancelledAt(courseMemberTracking.getCancelledAt());
      result.setExpiredAt(courseMemberTracking.getExpiredAt());
      result.setEnrolledAt(courseMemberTracking.getEnrolledAt());
      result.setCertificate(courseMemberTracking.getCertificate());
    }
    return result;
  }
}
