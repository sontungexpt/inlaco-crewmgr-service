package com.inlaco.crewmgrservice.feature.course.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inlaco.crewmgrservice.feature.course.domain.enums.CourseMemberStatus;
import java.time.Instant;
import lombok.Data;

@Data
public class CourseMemberInfoResponse {

  private String courseId;

  private String userId;

  private CourseMemberStatus status;

  private short completionProgress;

  private String note;

  public String getNote() {
    return note != null ? note : "";
  }

  private Instant completedAt; // The timestamp for course completion

  public boolean isCompleted() {
    return completedAt != null;
  }

  private Instant forciblyFinishedAt;

  public boolean isForciblyFinished() {
    return forciblyFinishedAt != null;
  }

  private Instant cancelledAt;

  public boolean isCancelled() {
    return cancelledAt != null;
  }

  private String certificateUrl;

  private Instant expiredAt;

  @JsonProperty("enrolledAt")
  private Instant createdAt;

  // private SailorProfileDTO sailorProfile;
}
