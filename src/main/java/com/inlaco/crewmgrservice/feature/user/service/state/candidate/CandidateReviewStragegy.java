package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.post.application.port.in.PostUseCase;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CandidateReviewStragegy {

  protected final CandidateProfileRepository candidateProfileRepository;
  protected final NotificationFactory notificationFactory;
  protected final PostUseCase postService;
  protected final UserService userService;

  public abstract void review(CandidateProfile candidateProfile, boolean autoEmail);

  public void review(String candidateId, boolean autoEmail) {
    review(getCandidateProfile(candidateId), autoEmail);
  }

  public void review(String candidateId) {
    review(candidateId, true);
  }

  public abstract void updateProfileStatus(CandidateProfile candidateProfile);

  public CandidateProfile getCandidateProfile(String id) {
    return candidateProfileRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(CandidateProfile.class, "id", id));
  }
}
