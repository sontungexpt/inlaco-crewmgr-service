package com.inlaco.crewmgrservice.feature.user.service.state;

import com.inlaco.crewmgrservice.feature.user.model.User;

public class SailorState extends UserJobState {

  public SailorState(User user) {
    super(user);
  }

  @Override
  public void promote(UserStateContext context) {
    return;
  }

  @Override
  public void demote(UserStateContext context) {
    context.setState(new BasicState(user));
  }
}
