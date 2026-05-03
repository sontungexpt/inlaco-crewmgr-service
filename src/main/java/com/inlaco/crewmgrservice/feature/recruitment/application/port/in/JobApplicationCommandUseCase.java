package com.inlaco.crewmgrservice.feature.recruitment.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;

public interface JobApplicationCommandUseCase {

  JobApplication apply(
      String recruitmentPostId, JobApplication application, String resumePublicId, User user);

  JobApplication updateApplication(String id, JsonNode patch, User user);
}
