package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking;
import com.inlaco.crewmgrservice.feature.course.model.CourseMemberTracking.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CourseEnrollment {

  private String id;

  @Schema(description = "The name of the course", example = "Cách xử lí khi có cháy trên thuyền")
  private String name;

  @Schema(
      description = "The ID of the course that this course is reopened based on",
      example = "60f7b3b3b3b3b3b3b3b3b3b3")
  private String reopenedBasedOn;

  @Schema(hidden = true)
  private String slug;

  @Min(1)
  @Schema(description = "The limit of students in the course", example = "100")
  @Default
  private int limitStudent = Integer.MAX_VALUE;

  @Schema(description = "Description of the course", example = "This is the course")
  private String description;

  @Schema(description = "The name of the teacher", example = "Nguyễn Văn A")
  private String teacherName;

  @Schema(description = "The start date of the course", example = "2021-09-01T00:00:00Z")
  private Instant startDate;

  @Schema(description = "The end date of the course", example = "2021-09-01T00:00:00Z")
  private Instant endDate;

  @Schema(description = "The status of the course member", example = "IN_PROGRESS")
  private Status status;

  @Schema(description = "The percent of completion progress in the course", example = "0")
  @Default
  private short completionProgress = 0;

  @Schema(description = "The note of the course member", example = "Passed the exam")
  private String note;

  @Schema(
      description = "The time the course was marked as finished",
      example = "2021-09-08T00:00:00Z")
  private Instant forciblyFinishedAt;

  @Schema(description = "The time the course was cancelled", example = "2021-09-06T00:00:00Z")
  private Instant cancelledAt;

  @Schema(description = "The time the course expired", example = "2021-09-10T00:00:00Z")
  private Instant expiredAt;

  @Schema(
      description = "The enrollment date of the course member",
      example = "2021-09-01T00:00:00Z")
  private Instant enrolledAt;

  @Schema(description = "The created date of the course", example = "2021-08-01T00:00:00Z")
  private Instant createdAt;

  @Schema(description = "The updated date of the course", example = "2021-08-10T00:00:00Z")
  private Instant updatedAt;

  @Schema(
      description = "The URL of the certificate for the course",
      example = "http://example.com/certificate.pdf")
  private String certificateUrl;

  @Schema(description = "Indicates if the course is currently expired", example = "false")
  public boolean isExpired() {
    return endDate != null && endDate.isBefore(Instant.now());
  }

  @Schema(description = "Indicates if the course is cancelled", example = "true")
  public boolean isCanceled() {
    return cancelledAt != null;
  }

  @Schema(description = "Indicates if the course is finished", example = "true")
  public boolean isForciblyFinished() {
    return forciblyFinishedAt != null;
  }

  public static CourseEnrollment from(Course course, CourseMemberTracking courseMemberTracking) {
    return CourseEnrollment.builder()
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
