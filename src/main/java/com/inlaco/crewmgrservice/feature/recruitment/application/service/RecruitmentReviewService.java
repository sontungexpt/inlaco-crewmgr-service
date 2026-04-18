package com.inlaco.crewmgrservice.feature.recruitment.application.service;

import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
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
    log.info("Starting review for application ID: {}", applicationId);
    JobApplication application =
        jobApplicationRepository
            .findById(applicationId)
            .orElseThrow(
                () -> {
                  log.warn("Job application not found with ID: {}", applicationId);
                  return new ResourceNotFoundException(JobApplication.class, "id", applicationId);
                });

    // Change state (Domain validation)
    try {
      application.changeStatus(newStatus);
      jobApplicationRepository.save(application);
      log.info("Application ID: {} status successfully changed to {}", applicationId, newStatus);
      application.broadcast(eventPublisher::publishEvent);
    } catch (IllegalStateException e) {
      log.warn("Failed to change application ID: {} status to {}", applicationId, newStatus);
      throw e;
    }
  }
}
