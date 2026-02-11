package com.inlaco.crewmgrservice.feature.recruitment.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.post.domain.enums.PostType;
import com.inlaco.crewmgrservice.feature.post.domain.exception.PostInactiveException;
import com.inlaco.crewmgrservice.feature.post.domain.model.Post;
import com.inlaco.crewmgrservice.feature.post.domain.model.RecruitmentPost;
import com.inlaco.crewmgrservice.feature.recruitment.application.event.ApplicationSubmittedEvent;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.JobApplicationUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.out.JobApplicationRepository;
import com.inlaco.crewmgrservice.feature.recruitment.domain.model.JobApplication;
import com.inlaco.crewmgrservice.feature.upload.application.enums.UploadStrategy;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadFactory;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobApplicationService implements JobApplicationUseCase {

  private final JobApplicationRepository jobApplicationRepository;
  private final PostUseCase postUseCase;
  private final UploadFactory uploadFactory;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public JobApplication apply(
      String recruitmentPostId, JobApplication application, String resumePublicId, User user) {
    Post post = postUseCase.getPost(recruitmentPostId);
    if (post instanceof RecruitmentPost recruitmentPost) {
      if (!recruitmentPost.isActive()) {
        throw new PostInactiveException("The registration post is closed");
      }

      File resume = uploadFactory.metadata(UploadStrategy.RESUME, resumePublicId);
      application.setPosition(recruitmentPost.getPosition());
      application.setResume(resume);
      application.setRecruitmentPostId(recruitmentPostId);
      application.setAccountId(user.getId());

      var newApplication = jobApplicationRepository.save(application);

      eventPublisher.publishEvent(new ApplicationSubmittedEvent(newApplication, recruitmentPost));

      return newApplication;
    }

    throw new ResourceNotFoundException(
        Post.class, Map.of("id", recruitmentPostId, "type", PostType.RECRUITMENT));
  }

  @Override
  public JobApplication updateApplication(String id, JsonNode patch, User user) {
    throw new UnsupportedOperationException("Unimplemented method 'updateApplication'");
  }

  @Override
  public Page<JobApplication> getMyApplication(User me, Pageable pageable) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getMyApplication'");
  }
}
