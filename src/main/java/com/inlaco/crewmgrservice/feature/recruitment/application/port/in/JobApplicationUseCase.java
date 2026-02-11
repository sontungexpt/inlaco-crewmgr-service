package com.inlaco.crewmgrservice.feature.recruitment.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationUseCase {

  JobApplication apply(
      String recruitmentPostId, JobApplication application, String resumePublicId, User user);

  JobApplication updateApplication(String id, JsonNode patch, User user);

  Page<JobApplication> getMyApplication(User me, Pageable pageable);
}
