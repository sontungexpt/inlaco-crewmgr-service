package com.inlaco.crewmgrservice.feature.user.service.state;

import com.inlaco.crewmgrservice.feature.user.model.User;

public class CandidateState extends UserJobState {

  public CandidateState(User user) {
    super(user);
  }

  @Override
  public void promote(UserStateContext context) {
    context.setState(new SailorState(user));
  }

  @Override
  public void demote(UserStateContext context) {
    context.setState(new BasicState(user));
  }
}
