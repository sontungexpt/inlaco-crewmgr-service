package com.inlaco.crewmgrservice.feature.user.model.state.job;

import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.model.User.JobState;

public class SailorState extends UserJobState {

  public SailorState(User user) {
    super(user);
  }

  @Override
  public void promote(CircleJobStateContext context) {
    return;
  }

  @Override
  public void demote(CircleJobStateContext context) {
    context.setState(new CanApplyState(user));
  }

  @Override
  public void updateUser() {
    user.setJobState(JobState.SAILOR);
  }
}
