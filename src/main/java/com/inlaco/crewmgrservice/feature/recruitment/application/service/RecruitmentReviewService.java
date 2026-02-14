package com.inlaco.crewmgrservice.feature.recruitment.application.service;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecruitmentReviewService implements RecruitmentReviewUseCase {

  private final JobApplicationRepository jobApplicationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void reviewApplication(String applicationId, ApplicationStatus newStatus) {
    JobApplication application =
        jobApplicationRepository
            .findById(applicationId)
            .orElseThrow(
                () -> new ResourceNotFoundException(JobApplication.class, "id", applicationId));

    // 1️⃣ Change state (Domain validation)
    application.changeStatus(newStatus);
    jobApplicationRepository.save(application);
    application.broadcast(eventPublisher::publishEvent);
  }
}
