package com.inlaco.crewmgrservice.feature.user.model.state.job;

import com.inlaco.crewmgrservice.feature.user.model.User;

public abstract class UserJobState {

  protected User user;

  public UserJobState(User user) {
    this.user = user;
    updateUser();
  }

  public User.JobState getJobState() {
    return user.getJobState();
  }

  public abstract void updateUser();

  public abstract void promote(CircleJobStateContext context);

  public abstract void demote(CircleJobStateContext context);

  public void handleBeforePromote(UserJobState oldState) {}

  public void handleAfterPromote(UserJobState oldState) {}

  public void handleBeforeDemote(UserJobState oldState) {}

  public void handleAfterDemote(UserJobState oldState) {}
}
