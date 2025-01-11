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

  public void review(CandidateProfile.Status status, String id, boolean autoEmail) {
    String state;
    switch (status) {
      case REJECTED:
        state = ReviewService.REJECTED;
        break;
      case HIRED:
        state = ReviewService.HIRED;
        break;
      case WAIT_FOR_INTERVIEW:
        state = ReviewService.WAIT_INTERVIEW;
        break;
      default:
        throw new IllegalArgumentException("Invalid status: " + status);
    }
    getState(state).review(id, autoEmail);
  }
}
