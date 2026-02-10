package com.inlaco.crewmgrservice.feature.course.presentation.dto.response;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import java.time.Instant;
import lombok.Data;

@Data
public class UserCourseResponse {
  private CourseResponse course;

  private CourseMemberStatus status;

  private short completionProgress;

  private String note;

  private Instant cancelledAt;

  private Instant enrolledAt;

  private File certificate;

  public boolean isEnrolled() {
    return enrolledAt != null;
  }

  public boolean isCanceled() {
    return cancelledAt != null;
  }
}
