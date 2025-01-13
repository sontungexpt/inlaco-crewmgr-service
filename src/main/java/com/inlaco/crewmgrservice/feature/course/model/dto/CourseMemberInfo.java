package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import com.inlaco.crewmgrservice.feature.user.dto.SailorProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public class CourseMemberInfo {

  @Schema(description = "The ID of the course associated with this tracking record")
  private ObjectId courseId;

  @JsonGetter("courseId")
  public String getCourseIdStr() {
    return courseId.toHexString();
  }

  @Schema(description = "The ID of the user associated with this tracking record")
  private ObjectId userId;

  @JsonGetter("userId")
  public String getUserIdSrt() {
    return userId.toHexString();
  }

  @Schema(description = "The current status of the course member", ref = "Status", enumAsRef = true)
  private CourseMember.Status status;

  @Schema(description = "The completion percentage of the course", example = "0")
  private short completionProgress;

  @Schema(description = "A note about the course member")
  private String note;

  public String getNote() {
    return note != null ? note : "";
  }

  @Schema(description = "The timestamp when the course was marked as completed")
  private Instant completedAt; // The timestamp for course completion

  /**
   * CheckJsonValues if the course member has completed the course.
   *
   * @return true if completed, false otherwise
   */
  public boolean isCompleted() {
    return completedAt != null;
  }

  @Schema(description = "The timestamp for forced finish (e.g., expiration, admin action)")
  private Instant forciblyFinishedAt;

  /**
   * Checks if the course member was forcibly finished.
   *
   * @return true if forcibly finished, false otherwise
   */
  public boolean isForciblyFinished() {
    return forciblyFinishedAt != null;
  }

  @Schema(description = "The timestamp when the course was cancelled")
  private Instant cancelledAt; // The timestamp for course cancellation

  public boolean isCancelled() {
    return cancelledAt != null;
  }

  @Schema(description = "The URL of the certificate for the course")
  private String certificateUrl;

  @Schema(description = "The timestamp when the course was marked as expired")
  private Instant expiredAt;

  @Schema(description = "The enrollment timestamp for the course member")
  @JsonProperty("enrolledAt")
  private Instant createdAt;

  @Schema(description = "The last update timestamp for the course member")
  private SailorProfileDTO sailorProfile;
}
