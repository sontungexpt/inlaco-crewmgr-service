package com.inlaco.crewmgrservice.feature.recruitment.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.exception.PostInactiveException;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.recruitment.application.event.JobApplicationSubmittedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationCommandUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobApplicationCommandService implements JobApplicationCommandUseCase {

  private final JobApplicationRepository jobApplicationRepository;
  private final PostUseCase postUseCase;
  private final UploadDispatcher uploadDispatcher;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public JobApplication apply(
      String recruitmentPostId, JobApplication application, String resumePublicId, User user) {
    log.debug("Fetching recruitment post with ID: {}", recruitmentPostId);
    Post post = postUseCase.getPost(recruitmentPostId);

    if (!(post instanceof RecruitmentPost recruitmentPost)) {
      log.warn("Recruitment post not found with ID: {}", recruitmentPostId);
      throw new ResourceNotFoundException(
          Post.class, Map.of("id", recruitmentPostId, "type", PostType.RECRUITMENT));
    }

    if (!recruitmentPost.isActive()) {
      log.info("Recruitment post with ID: {} is inactive", recruitmentPostId);
      throw new PostInactiveException("The registration post is closed");
    }

    log.debug("Fetching resume asset with ID: {}", resumePublicId);
    Asset resume = uploadDispatcher.fetch(AssetType.RESUME, resumePublicId);
    application.setPosition(recruitmentPost.getPosition());
    application.setResume(resume);
    application.setRecruitmentPostId(recruitmentPostId);
    application.setAccountId(user.getId());

    log.info(
        "Saving job application for user ID: {} and recruitment post ID: {}",
        user.getId(),
        recruitmentPostId);
    var newApplication = jobApplicationRepository.save(application);

    log.info("Publishing ApplicationSubmittedEvent for application ID: {}", newApplication.getId());
    eventPublisher.publishEvent(new JobApplicationSubmittedEvent(newApplication, recruitmentPost));

    return newApplication;
  }

  @Override
  public JobApplication updateApplication(String id, JsonNode patch, User user) {
    throw new UnsupportedOperationException("Unimplemented method 'updateApplication'");
  }
}
