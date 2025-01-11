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
    value = {"id"},
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

  @Schema(description = "Is the course forcibly canceled", example = "false")
  @Default
  private boolean forciblyCanceled = false;

  @Schema(
      description = "The time the course was forcibly canceled",
      example = "2021-09-06T00:00:00Z")
  private Instant forciblyCanceledAt;

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

  public void forceCancel() {
    this.forciblyCanceled = true;
    this.forciblyCanceledAt = Instant.now();
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
  @Schema(description = "Is the course deleted", hidden = true)
  private boolean deleted = false;

  @Schema(description = "The time the course was deleted", example = "2021-09-06T00:00:00Z")
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

  @Schema(description = "Description of the course", example = "This is the course")
  @NotBlank
  private String description;

  @Schema(description = "The start date of the course", example = "2021-09-01T00:00:00Z")
  @FutureOrPresent
  @DateTimeFormat
  @Default
  private Instant startDate = Instant.now();

  @Schema(description = "Is the registration enabled", example = "true")
  @Default
  private boolean registrationDisabled = false;

  private Instant registrationDisabledAt;

  public void disableRegistration() {
    this.registrationDisabled = true;
    this.registrationDisabledAt = Instant.now();
  }

  public Instant getRegistrationDisabledAt() {
    return registrationDisabledAt == null ? endRegistrationAt : registrationDisabledAt;
  }

  public boolean isRegistrationDisabled() {
    var now = Instant.now();
    return !registrationDisabled
        && now.isAfter(startRegistrationAt)
        && now.isBefore(endRegistrationAt);
  }

  @Schema(
      description = "The start registration date of the course",
      example = "2021-09-01T00:00:00Z")
  @FutureOrPresent
  @DateTimeFormat
  @Default
  private Instant startRegistrationAt = Instant.now();

  @Schema(description = "The end registration date of the course", example = "2021-09-01T00:00:00Z")
  @Future
  @DateTimeFormat
  private Instant endRegistrationAt;

  @Override
  public List<Pair<Instant, Instant>> getTimeFrames() {
    return List.of(Pair.of(startDate, endDate), Pair.of(startRegistrationAt, endRegistrationAt));
  }

  @Schema(description = "The end date of the course", example = "2021-09-01T00:00:00Z")
  @Future
  @DateTimeFormat
  private Instant endDate;

  @Schema(description = "The wallpaper url")
  private File wallpaper;

  @Schema(description = "Is the course expired", example = "false", hidden = true)
  public boolean isExpired() {
    return endDate.isBefore(Instant.now());
  }

  @Schema(hidden = true)
  @CreatedBy
  @JsonIgnore
  private ObjectId createdBy;

  @Schema(hidden = true)
  @LastModifiedBy
  @JsonIgnore
  private ObjectId updatedBy;

  @Schema(hidden = true)
  @CreatedDate
  @JsonIgnore
  private Instant createdAt;

  @Schema(hidden = true)
  public Instant getCreatedDate() {
    return createdAt;
  }

  @Schema(hidden = true)
  @LastModifiedDate
  @JsonIgnore
  private Instant updatedAt;

  @Schema(hidden = true)
  public Instant getUpdatedDate() {
    return updatedAt;
  }
}
