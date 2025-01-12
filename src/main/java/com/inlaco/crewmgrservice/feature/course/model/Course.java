package com.inlaco.crewmgrservice.feature.course.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.AutoSlugify;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.model.Sluggable;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

@Document(collection = "courses")
@JsonIgnoreProperties(
    value = {
      "id",
      "forciblyCanceledAt",
      "deleted",
      "deletedAt",
      "reopenedBasedOn",
      "enrolledStudentCount",
      "manuallyRegistrationDisabled",
      "manuallyRegistrationDisabledAt",
    },
    allowGetters = true)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course implements Sluggable<String>, Cloneable, Serializable, TimeFrame {
  @Schema(hidden = true)
  @Id
  private String id;

  @Schema(description = "The name of the course", example = "Cách xử lí khi có cháy trên thuyền")
  @NotBlank
  private String name;

  @Schema(
      description = "The ID of the course that this course is reopened based on",
      example = "null",
      hidden = true,
      examples = {"60f7b3b3b3b3b3b3b3b3b3b3", "null"})
  private ObjectId reopenedBasedOn;

  @Schema(description = "The training provider name", example = "Công ty TNHH ABC")
  private String trainingProviderName;

  @Schema(description = "The training provider logo url")
  private String trainingProviderLogo;

  @Schema(description = "The name of the teacher", example = "Nguyễn Văn A")
  private String teacherName;

  @Schema(description = "The achieved position after complete the course", example = "Captain")
  private String achievedPosition;

  @Schema(description = "Is the course provide an certification", example = "true")
  private boolean certified;

  @Schema(description = "The time the course was forcibly canceled", hidden = true)
  private Instant forciblyCanceledAt;

  public void forceCancel() {
    this.forciblyCanceledAt = Instant.now();
  }

  @JsonIgnore
  public boolean isForciblyCanceled() {
    return forciblyCanceledAt != null;
  }

  public String getTeacherName() {
    if (StringUtils.hasText(teacherName)) {
      return teacherName;
    } else {
      return "In upcoming";
    }
  }

  public Course clone() throws CloneNotSupportedException {
    return (Course) this.clone();
  }

  public Course reopenCourse(Instant startDate, Instant endDate) throws CloneNotSupportedException {
    Course reopenedCourse = this.clone();
    reopenedCourse.reopenedBasedOn = new ObjectId(this.id);
    reopenedCourse.startDate = startDate;
    reopenedCourse.endDate = endDate;

    // reset some fields to null to make sure that field will be auto gen by mongodb
    reopenedCourse.id = null;
    reopenedCourse.slug = null;
    reopenedCourse.createdAt = null;
    reopenedCourse.updatedAt = null;
    reopenedCourse.createdBy = null;
    reopenedCourse.updatedBy = null;
    return reopenedCourse;
  }

  @AutoSlugify(fields = "name")
  @Schema(hidden = true)
  private String slug;

  @Default
  @JsonIgnore
  @Schema(hidden = true, description = "Is the course deleted")
  private boolean deleted = false;

  @Schema(description = "The time the course was deleted", hidden = true)
  @JsonIgnore
  private Instant deletedAt;

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
    if (deleted) {
      deletedAt = Instant.now();
    }
  }

  @Min(1)
  @Schema(description = "The limit of student in the course", example = "100")
  @Default
  private int limitStudent = Integer.MAX_VALUE;

  @Default
  @Min(0)
  @Schema(description = "The count of student enrolled in the course", hidden = true)
  private int enrolledStudentCount = 0;

  public void increaseEnrolledStudentCount() {
    if (enrolledStudentCount < limitStudent) {
      enrolledStudentCount++;
    }
  }

  public boolean isFull() {
    return enrolledStudentCount >= limitStudent;
  }

  @Schema(description = "Description of the course", example = "This is the course")
  @NotBlank
  private String description;

  @Schema(description = "Is the registration enabled", hidden = true)
  @Default
  private boolean manuallyRegistrationDisabled = false;

  @Schema(description = "The time the registration was disabled", example = "2021-09-06T00:00:00Z")
  private Instant manuallyRegistrationDisabledAt;

  public Instant getManuallyRegistrationDisabledAt() {
    return manuallyRegistrationDisabledAt == null
        ? endRegistrationAt
        : manuallyRegistrationDisabledAt;
  }

  public void manuallyDisableRegistration() {
    this.manuallyRegistrationDisabled = true;
    this.manuallyRegistrationDisabledAt = Instant.now();
  }

  public boolean inRegistrationPeriod() {
    var now = Instant.now();
    return now.isAfter(startRegistrationAt)
        && (endRegistrationAt == null || now.isBefore(endRegistrationAt));
  }

  public boolean isRegistrationEnabled() {
    return !manuallyRegistrationDisabled && !isFull() && inRegistrationPeriod();
  }

  @Schema(
      description = "The start registration date of the course",
      example = "2021-09-01T00:00:00Z")
  @FutureOrPresent
  @DateTimeFormat
  @Default
  private Instant startRegistrationAt = Instant.now().plusSeconds(30);

  @Schema(description = "The end registration date of the course", example = "2021-09-01T00:00:00Z")
  @DateTimeFormat
  @Future
  private Instant endRegistrationAt;

  @Default
  @Schema(description = "The start date of the course", example = "2021-09-01T00:00:00Z")
  @FutureOrPresent
  @DateTimeFormat
  private Instant startDate = Instant.now().plusSeconds(30);

  @Future
  @DateTimeFormat
  @Schema(description = "The end date of the course", example = "2021-09-01T00:00:00Z")
  private Instant endDate;

  @Schema(description = "The wallpaper url")
  private File wallpaper;

  @Schema(description = "Is the course expired", example = "false", hidden = true)
  public boolean isExpired() {
    return endDate.isBefore(Instant.now());
  }

  @CreatedBy
  @JsonIgnore
  @Schema(hidden = true)
  private ObjectId createdBy;

  @LastModifiedBy
  @JsonIgnore
  @Schema(hidden = true)
  private ObjectId updatedBy;

  @CreatedDate
  @JsonIgnore
  @Schema(hidden = true)
  private Instant createdAt;

  @Schema(hidden = true)
  public Instant getCreatedDate() {
    return createdAt;
  }

  @LastModifiedDate
  @JsonIgnore
  @Schema(hidden = true)
  private Instant updatedAt;

  @Schema(hidden = true)
  public Instant getUpdatedDate() {
    return updatedAt;
  }

  @Override
  @JsonIgnore
  @Schema(hidden = true)
  public List<Pair> getTimeFrames() {
    return List.of(
        Pair.of(startDate, endDate), Pair.of(startRegistrationAt, endRegistrationAt, true, false));
  }
}
