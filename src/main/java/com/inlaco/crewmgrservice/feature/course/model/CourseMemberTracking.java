package com.inlaco.crewmgrservice.feature.course.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the tracking information for a course member, including progress, status, and key
 * timestamps.
 */
@Data
@Document(collection = "course_member_trackings")
@JsonIgnoreProperties(
    value = {"id", "certificateUrl", "userId", "courseId", "status"},
    allowGetters = true)
public class CourseMemberTracking {

  @Id private String id; // Unique identifier for the course member tracking document

  @Schema(description = "The ID of the course associated with this tracking record", hidden = true)
  private ObjectId courseId;

  @Schema(description = "The ID of the user associated with this tracking record", hidden = true)
  @Indexed
  private ObjectId userId;

  /** Enum representing the various statuses of the course member. */
  public enum Status {
    PENDING, // The course is pending to start
    IN_PROGRESS, // The course is currently in progress
    COMPLETED, // The course has been successfully completed
    EXPIRED, // The course has expired
    CANCELLED, // The course was cancelled
    UNKNOWN // The status is unknown
  }

  @Schema(
      description = "The current status of the course member",
      ref = "Status",
      hidden = true,
      enumAsRef = true)
  private Status status;

  @Schema(hidden = true, description = "The completion percentage of the course", example = "0")
  @Min(0) // Minimum completion progress
  @Max(100) // Maximum completion progress
  private short completionProgress = 0;

  @Schema(description = "A note about the course member", example = "Passed the exam")
  private String note; // A note or comment about the course member's progress

  @Schema(description = "The timestamp when the course was marked as completed", hidden = true)
  private Instant completedAt; // The timestamp for course completion

  /**
   * Checks if the course member has completed the course.
   *
   * @return true if completed, false otherwise
   */
  public boolean isCompleted() {
    return completedAt != null;
  }

  /** Marks the course member as completed and sets the completedAt timestamp. */
  public void complete() {
    this.completedAt = Instant.now();
  }

  @JsonIgnore
  @Schema(description = "The timestamp when the course was forcibly finished", hidden = true)
  private Instant
      forciblyFinishedAt; // The timestamp for forced finish (e.g., expiration, admin action)

  /**
   * Checks if the course member was forcibly finished.
   *
   * @return true if forcibly finished, false otherwise
   */
  public boolean isForciblyFinished() {
    return forciblyFinishedAt != null;
  }

  /** Marks the course as forcibly finished and sets the forcedFinishAt timestamp. */
  public void forceFinished() {
    this.forciblyFinishedAt = Instant.now();
  }

  @JsonIgnore
  @Schema(description = "The timestamp when the course was cancelled", hidden = true)
  private Instant cancelledAt; // The timestamp for course cancellation

  /**
   * Checks if the course was cancelled.
   *
   * @return true if cancelled, false otherwise
   */
  public boolean isCancelled() {
    return cancelledAt != null;
  }

  /** Cancels the course and sets the cancelledAt timestamp. */
  public void cancel() {
    this.cancelledAt = Instant.now();
  }

  @Schema(hidden = true)
  private String certificateUrl; // The URL of the certificate for the course

  @Schema(description = "The timestamp when the course was marked as expired", hidden = true)
  @JsonIgnore
  private Instant expiredAt; // The timestamp when the course expired

  @CreatedDate
  @Schema(description = "The enrollment timestamp for the course member", hidden = true)
  @JsonIgnore
  private Instant createdAt; // The timestamp for document creation

  /**
   * Gets the enrolled timestamp, represented by the createdAt field.
   *
   * @return the enrolled timestamp
   */
  public Instant getEnrolledAt() {
    return createdAt;
  }

  public boolean isEnrolled() {
    return createdAt != null;
  }

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  private Instant updatedAt; // The timestamp for the last update
}
