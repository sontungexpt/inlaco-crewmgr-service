package com.inlaco.crewmgrservice.feature.user.service.state.candidate;

import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceFactory {

  private final Map<String, CandidateReviewStragegy> candidateServiceStateMap;

  public CandidateReviewStragegy getState(String state) {
    if (candidateServiceStateMap.containsKey(state)) {
      return candidateServiceStateMap.get(state);
    }
    throw new IllegalArgumentException("Invalid state: " + state);
  }

  public void review(String state, String id) {
    getState(state).review(id);
  }

  public String getServiceName(CandidateProfile.Status status) {
    switch (status) {
      case HIRED:
        return ReviewService.HIRED;
      case REJECTED:
        return ReviewService.REJECTED;
      case WAIT_FOR_INTERVIEW:
        return ReviewService.WAIT_INTERVIEW;
      default:
        throw new IllegalArgumentException("Invalid status: " + status);
    }
  }

  public void review(CandidateProfile.Status status, CandidateProfile profile, boolean autoEmail) {
    getState(getServiceName(status)).review(profile, autoEmail);
  }

  public void review(CandidateProfile.Status status, String id, boolean autoEmail) {
    getState(getServiceName(status)).review(id, autoEmail);
  }
}
