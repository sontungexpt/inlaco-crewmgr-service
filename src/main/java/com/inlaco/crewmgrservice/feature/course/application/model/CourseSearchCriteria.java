package com.inlaco.crewmgrservice.feature.course.application.model;

import lombok.Data;

@Data
public class CourseSearchCriteria {
  private String keyword = null;
  private String accountId = null;
  private Boolean nonExpired = true;
  private Boolean registrationEnabled = null;
}
