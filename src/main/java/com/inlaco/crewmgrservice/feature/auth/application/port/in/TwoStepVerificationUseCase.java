package com.inlaco.crewmgrservice.feature.auth.application.port.in;

import com.inlaco.crewmgrservice.feature.user.model.User;

public interface TwoStepVerificationUseCase {

  void resend(User user);

  void verify(String token);
}
