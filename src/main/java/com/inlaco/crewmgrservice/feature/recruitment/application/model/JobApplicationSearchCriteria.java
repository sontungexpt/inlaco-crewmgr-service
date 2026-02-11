package com.inlaco.crewmgrservice.feature.recruitment.application.model;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import lombok.Data;

@Data
public class JobApplicationSearchCriteria {
  ApplicationStatus status;
  String recruitmentPostId;
  String accountId;
}
