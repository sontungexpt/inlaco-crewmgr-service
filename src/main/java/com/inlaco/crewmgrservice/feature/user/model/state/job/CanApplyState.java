package com.inlaco.crewmgrservice.feature.user.model.state.job;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.User.JobState;

public class CanApplyState extends UserJobState {

  public CanApplyState(User user) {
    super(user);
  }

  @Override
  public void promote(CircleJobStateContext context) {
    context.setState(new CandidateState(user));
  }

  @Override
  public void demote(CircleJobStateContext context) {
    context.setState(new CanApplyState(user));
  }

  @Override
  public void updateUser() {
    user.setJobState(JobState.CAN_APPLY);
  }
}
