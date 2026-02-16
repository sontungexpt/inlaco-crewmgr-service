package com.inlaco.crewmgrservice.feature.course.presentation.dto.response;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class CourseResponse implements Serializable {

  private String id;

  private String name;

  private String trainingProviderName;

  private Asset trainingProviderLogo;

  private String teacherName;

  public String getTeacherName() {
    return StringUtils.hasText(teacherName) ? teacherName : "In upcoming";
  }

  private String archivedPosition;

  private boolean certified;

  private Instant forciblyCanceledAt;

  private int limitStudent;

  private int enrolledStudentCount;

  private Asset wallpaper;

  private String description;

  private Instant manuallyRegistrationDisabledAt;

  private Instant startRegistrationAt;

  private Instant endRegistrationAt;

  private Instant startDate;

  private Instant endDate;

  private Instant createdAt;

  private Instant updatedAt;

  private boolean full;

  private boolean forciblyCanceled;

  private boolean registrationEnabled;

  private boolean expired;

  private boolean learnable;
}
