package com.inlaco.crewmgrservice.feature.recruitment.application.port.in;

import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;

public interface JobApplicationReviewUseCase {

  void reviewApplication(String applicationId, ApplicationStatus status);
}
