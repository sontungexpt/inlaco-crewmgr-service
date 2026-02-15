package com.inlaco.crewmgrservice.feature.course.domain.model;

import com.inlaco.crewmgrservice.common.model.Asset;
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

  public void forceCancel() {
    this.forciblyCanceledAt = Instant.now();
  }

  public boolean isForciblyCanceled() {
    return forciblyCanceledAt != null;
  }

  private int limitStudent;

  private int enrolledStudentCount = 0;

  public void increaseEnrolledStudentCount() {
    if (enrolledStudentCount < limitStudent) {
      enrolledStudentCount++;
    }
  }

  public boolean isFull() {
    return enrolledStudentCount >= limitStudent;
  }

  private Asset wallpaper;

  private String description;

  private Instant manuallyRegistrationDisabledAt;

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

  private Instant startRegistrationAt;

  private Instant endRegistrationAt;

  private Instant startDate;

  private Instant endDate;

  public boolean isExpired() {
    return endDate.isBefore(Instant.now());
  }

  public boolean isLearnable() {
    return !isForciblyCanceled() && !isExpired();
  }

  private ObjectId createdBy;

  private ObjectId updatedBy;

  private Instant createdAt;

  private Instant updatedAt;
}
