package com.inlaco.crewmgrservice.feature.recruitment.application.port.out;

import com.inlaco.crewmgrservice.feature.recruitment.application.model.JobApplicationSearchCriteria;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationRepository {
  Optional<JobApplication> findById(String id);

  Page<JobApplication> findByAccountId(String accountId, Pageable pageable);

  Page<JobApplication> findAll(Pageable pageable);

  Page<JobApplication> findAll(JobApplicationSearchCriteria criteria, Pageable pageable);

  JobApplication save(JobApplication jobApplication);
}
