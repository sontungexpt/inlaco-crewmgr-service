package com.inlaco.crewmgrservice.feature.course.domain.model;

import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseMember {

  private String id;

  private String courseId;

  private String userId;

  private CourseMemberStatus status = CourseMemberStatus.PENDING;

  private short completionProgress = 0;

  private String note;

  private Instant completedAt;

  public boolean isCompleted() {
    return completedAt != null;
  }

  public void complete() {
    this.completedAt = Instant.now();
    this.status = CourseMemberStatus.COMPLETED;
    this.completionProgress = 100;
  }

  private Instant forciblyFinishedAt;

  public boolean isForciblyFinished() {
    return forciblyFinishedAt != null;
  }

  public void forceFinished() {
    this.forciblyFinishedAt = Instant.now();
  }

  private Instant cancelledAt;

  public boolean isCancelled() {
    return cancelledAt != null;
  }

  public void cancel() {
    this.cancelledAt = Instant.now();
  }

  private Asset certificate;

  private Instant expiredAt;

  private Instant createdAt;

  public boolean isEnrolled() {
    return createdAt != null;
  }

  private Instant updatedAt;

  public CourseMember(String courseId, String userId) {
    this.courseId = courseId;
    this.userId = userId;
  }
}
