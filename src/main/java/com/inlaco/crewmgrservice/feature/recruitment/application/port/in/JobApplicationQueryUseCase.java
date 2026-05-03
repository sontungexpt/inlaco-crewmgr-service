package com.inlaco.crewmgrservice.feature.recruitment.application.port.in;

import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationQueryUseCase {

  Page<JobApplication> getAllApplications(JobApplicationSearchCriteria criteria, Pageable pageable);

  JobApplication getApplicationDetail(String applicationId);
}
