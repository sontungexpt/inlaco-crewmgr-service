package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile.Status;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(ReviewService.CONTRACT_NOT_YET_IN_FORCE)
public class CandidateContractNotYetInForceService extends CandidateReviewStragegy {

  public CandidateContractNotYetInForceService(
      CandidateProfileRepository candidateProfileRepository,
      NotificationFactory notificationFactory,
      PostService postService,
      UserService userService) {
    super(candidateProfileRepository, notificationFactory, postService, userService);
  }

  @Override
  public void updateProfileStatus(CandidateProfile profile) {
    profile.setStatus(Status.CONTRACT_NOT_YET_IN_FORCE);
    candidateProfileRepository.save(profile);
  }

  @Override
  public void review(CandidateProfile profile, boolean autoEmail) {
    if (profile.getStatus() == Status.CONTRACT_NOT_YET_IN_FORCE) return;
    log.debug("Contract not yet in force: {}", profile.getFullName());
    updateProfileStatus(profile);
  }
}
