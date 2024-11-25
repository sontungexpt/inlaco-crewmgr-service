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
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "course_member_trackings")
@JsonIgnoreProperties(
    value = {"id", "certificateUrl", "userId", "courseId", "status"},
    allowGetters = true)
public class CourseMemberTracking {

  @Id private String id;

  @Schema(
      description = "The ID of the course that this course member tracking is associated with",
      hidden = true)
  private ObjectId courseId;

  @Schema(
      description = "The ID of the user that this course member tracking is associated with",
      hidden = true)
  private ObjectId userId;

  public enum Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    EXPIRED,
    CANCELLED
  }

  @Schema(
      description = "The status of the course member tracking",
      ref = "Status",
      hidden = true,
      enumAsRef = true)
  private Status status;

  @Schema(
      hidden = true,
      description = "The percent of completion progress in the course",
      example = "0")
  @Min(0)
  @Max(100)
  private short completionProgress = 0;

  @Schema(description = "The note of the course member tracking", example = "Passed the exam")
  private String note;

  @JsonIgnore
  @Schema(hidden = true)
  private Instant finishedAt;

  public boolean isFinished() {
    return finishedAt != null;
  }

  public void finish() {
    Instant now = Instant.now();
    this.finishedAt = now;
  }

  @JsonIgnore
  @Schema(hidden = true)
  private Instant cancelledAt;

  @Schema(hidden = true)
  private String certificateUrl;

  public boolean isCanceled() {
    return cancelledAt != null;
  }

  public void cancel() {
    this.cancelledAt = Instant.now();
  }

  @Schema(hidden = true)
  @JsonIgnore
  private Instant expiredAt;

  @CreatedDate
  @Schema(hidden = true)
  @JsonIgnore
  private Instant createdAt;

  public Instant getEnrolledAt() {
    return createdAt;
  }

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  private Instant updatedAt;
}
