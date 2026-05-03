package com.inlaco.crewmgrservice.feature.recruitment.application.service;

import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationQueryUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobApplicationQueryService implements JobApplicationQueryUseCase {

  private final JobApplicationRepository jobApplicationRepository;

  @Override
  public Page<JobApplication> getAllApplications(
      JobApplicationSearchCriteria criteria, Pageable pageable) {
    log.info("Fetching all job applications with criteria: {}", criteria);
    return jobApplicationRepository.findAll(criteria, pageable);
  }

  @Override
  public JobApplication getApplicationDetail(String applicationId) {
    log.info("Fetching details for job application with ID: {}", applicationId);
    return jobApplicationRepository
        .findById(applicationId)
        .orElseThrow(
            () -> {
              log.warn("Job application not found with ID: {}", applicationId);
              return new ResourceNotFoundException(JobApplication.class, "id", applicationId);
            });
  }
}
