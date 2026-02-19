package com.inlaco.crewmgrservice.feature.course.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Course {

  private String id;
  private String name;
  private String trainingProviderName;
  private Asset trainingProviderLogo;
  private String teacherName;
  private String archivedPosition;
  private boolean certified;
  private Instant forciblyCanceledAt;
  private Asset wallpaper;
  private String description;
  private Instant manuallyRegistrationDisabledAt;
  private Instant startRegistrationAt;
  private Instant endRegistrationAt;
  private Instant startDate;
  private Instant endDate;
  private int limitStudent;
  private int enrolledStudentCount = 0;
  private ObjectId createdBy;
  private ObjectId updatedBy;
  private Instant createdAt;
  private Instant updatedAt;

  public void forceCancel() {
    this.forciblyCanceledAt = Instant.now();
  }

  public boolean isForciblyCanceled() {
    return forciblyCanceledAt != null;
  }

  public void increaseEnrolledStudentCount() {
    if (enrolledStudentCount < limitStudent) {
      enrolledStudentCount++;
    }
  }

  public boolean isFull() {
    return enrolledStudentCount >= limitStudent;
  }

  public void manuallyDisableRegistration() {
    this.manuallyRegistrationDisabledAt = Instant.now();
  }

  public boolean inRegistrationPeriod() {
    var now = Instant.now();
    return now.isAfter(startRegistrationAt)
        && (endRegistrationAt == null || now.isBefore(endRegistrationAt));
  }

  public boolean isRegistrationEnabled() {
    return manuallyRegistrationDisabledAt == null && !isFull() && inRegistrationPeriod();
  }

  public boolean isExpired() {
    return endDate.isBefore(Instant.now());
  }

  public boolean isLearnable() {
    return !isForciblyCanceled() && !isExpired();
  }

  public boolean update(CourseUpdateCommand command) {
    return command.getName().ifUpdated(this::setName)
        | command.getTrainingProviderName().ifUpdated(this::setTrainingProviderName)
        | command.getTrainingProviderLogo().ifUpdated(this::setTrainingProviderLogo)
        | command.getTeacherName().ifUpdated(this::setTeacherName)
        | command.getArchivedPosition().ifUpdated(this::setArchivedPosition)
        | command.getCertified().ifUpdated(this::setCertified)
        | command.getWallpaper().ifUpdated(this::setWallpaper)
        | command.getDescription().ifUpdated(this::setDescription)
        | command.getStartRegistrationAt().ifUpdated(this::setStartRegistrationAt)
        | command.getEndRegistrationAt().ifUpdated(this::setEndRegistrationAt)
        | command.getStartDate().ifUpdated(this::setStartDate)
        | command.getEndDate().ifUpdated(this::setEndDate)
        | command.getLimitStudent().ifUpdated(this::setLimitStudent);
  }
}
