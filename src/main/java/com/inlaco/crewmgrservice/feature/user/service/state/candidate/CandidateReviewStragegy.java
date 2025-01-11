package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.notify.NotificationFactory;
import com.inlaco.crewmgrservice.feature.post.service.PostService;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.repository.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CandidateReviewStragegy {
  protected final CandidateProfileRepository candidateProfileRepository;
  protected final NotificationFactory notificationFactory;
  protected final PostService postService;

  public abstract void review(String candidateId, boolean autoEmail);

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
