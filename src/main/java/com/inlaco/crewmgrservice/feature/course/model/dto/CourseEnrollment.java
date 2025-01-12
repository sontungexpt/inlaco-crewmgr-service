package com.inlaco.crewmgrservice.feature.course.model.dto;

import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.course.model.Course;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember;
import com.inlaco.crewmgrservice.feature.course.model.CourseMember.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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

  @Schema(description = "The training provider name", example = "Công ty TNHH ABC")
  private String trainingProviderName;

  @Schema(description = "The training provider logo url")
  private String trainingProviderLogo;

  @Schema(description = "The wallpaper of the course")
  private File wallpaper;

  @Schema(description = "The archived position of the course", example = "Deck")
  private String archivedPosition;

  @Schema(description = "Indicates if the course is certified", example = "true")
  private boolean certified;

  @Schema(hidden = true)
  private String slug;

  @Min(1)
  @Schema(description = "The limit of students in the course", example = "100")
  @Default
  private int limitStudent = Integer.MAX_VALUE;

  @Schema(description = "Description of the course", example = "This is the course")
  private String description;

  public String getDescription() {
    return description != null ? description : "";
  }

  @Schema(description = "The name of the teacher", example = "Nguyễn Văn A")
  private String teacherName;

  @Schema(description = "The start date of the course", example = "2021-09-01T00:00:00Z")
  private Instant startDate;

  @Schema(description = "The end date of the course", example = "2021-09-01T00:00:00Z")
  private Instant endDate;

  @Schema(description = "The status of the course member", example = "IN_PROGRESS")
  @Default
  private Status status = Status.UNKNOWN;

  @Schema(description = "The percent of completion progress in the course", example = "0")
  @Default
  private short completionProgress = 0;

  @Schema(description = "The note of the course member", example = "Passed the exam")
  private String note;

  public String getNote() {
    return note != null ? note : "";
  }

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

  public boolean isEnrolled() {
    return enrolledAt != null;
  }

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

  public static CourseEnrollment from(
      @NonNull Course course, @Nullable CourseMember courseMemberTracking) {
    var result =
        CourseEnrollment.builder()
            .id(course.getId())
            .name(course.getName())
            .slug(course.getSlug())
            .limitStudent(course.getLimitStudent())
            .wallpaper(course.getWallpaper())
            .trainingProviderName(course.getTrainingProviderName())
            .trainingProviderLogo(course.getTrainingProviderLogo())
            .certified(course.isCertified())
            .archivedPosition(course.getAchievedPosition())
            .description(course.getDescription())
            .teacherName(course.getTeacherName())
            .startDate(course.getStartDate())
            .endDate(course.getEndDate())
            .createdAt(course.getCreatedAt())
            .updatedAt(course.getUpdatedAt())
            .build();

    if (courseMemberTracking != null) {
      result.status = courseMemberTracking.getStatus();
      result.completionProgress = courseMemberTracking.getCompletionProgress();
      result.note = courseMemberTracking.getNote();
      result.forciblyFinishedAt = courseMemberTracking.getForciblyFinishedAt();
      result.cancelledAt = courseMemberTracking.getCancelledAt();
      result.expiredAt = courseMemberTracking.getExpiredAt();
      result.enrolledAt = courseMemberTracking.getEnrolledAt();
      result.certificateUrl = courseMemberTracking.getCertificateUrl();
    }
    return result;
  }
}
