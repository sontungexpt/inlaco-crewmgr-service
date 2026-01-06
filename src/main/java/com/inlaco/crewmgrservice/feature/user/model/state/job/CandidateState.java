package com.inlaco.crewmgrservice.feature.user.model.state.job;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.User.JobState;

public class CandidateState extends UserJobState {

  public CandidateState(User user) {
    super(user);
  }

  @Override
  public void promote(CircleJobStateContext context) {
    context.setState(new SailorState(user));
  }

  @Override
  public void demote(CircleJobStateContext context) {
    context.setState(new CanApplyState(user));
  }

  @Override
  public void updateUser() {
    user.setJobState(JobState.CANDIDATE);
  }
}
