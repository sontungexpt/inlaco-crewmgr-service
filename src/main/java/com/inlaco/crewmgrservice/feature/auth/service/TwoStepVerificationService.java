package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.user.model.User;

public interface TwoStepVerificationService {

  void send(User user);

  void resend(User user);

  void verify(String token);
}
