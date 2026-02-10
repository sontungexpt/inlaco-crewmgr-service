package com.inlaco.crewmgrservice.feature.course.presentation.dto.response;

import com.inlaco.crewmgrservice.common.model.File;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class CourseResponse implements Serializable {

  private String id;

  private String name;

  private String trainingProviderName;

  private File trainingProviderLogo;

  private String teacherName;

  private String archivedPosition;

  private boolean certified;

  private Instant forciblyCanceledAt;

  private int limitStudent = Integer.MAX_VALUE;

  private int enrolledStudentCount = 0;

  private File wallpaper;

  private String description;

  private Instant manuallyRegistrationDisabledAt;

  private Instant startRegistrationAt = Instant.now().plusSeconds(30);

  private Instant endRegistrationAt;

  private Instant startDate = Instant.now().plusSeconds(30);

  private Instant endDate;

  private Instant createdAt;

  private Instant updatedAt;

  public String getTeacherName() {
    return StringUtils.hasText(teacherName) ? teacherName : "In upcoming";
  }

  public boolean isFull() {
    return enrolledStudentCount >= limitStudent;
  }

  public boolean isForciblyCanceled() {
    return forciblyCanceledAt != null;
  }

  public Instant getManuallyRegistrationDisabledAt() {
    return manuallyRegistrationDisabledAt == null
        ? endRegistrationAt
        : manuallyRegistrationDisabledAt;
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
}
