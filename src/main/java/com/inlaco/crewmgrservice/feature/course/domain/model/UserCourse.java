package com.inlaco.crewmgrservice.feature.course.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCourse {
  private Course course;

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
