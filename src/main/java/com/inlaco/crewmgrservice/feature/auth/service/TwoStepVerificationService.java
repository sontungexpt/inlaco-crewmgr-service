package com.inlaco.crewmgrservice.feature.auth.service;

import com.inlaco.crewmgrservice.feature.auth.dto.ResendTokenResponse;
import com.inlaco.crewmgrservice.feature.user.model.User;

public interface TwoStepVerificationService {

  ResendTokenResponse send(User user);

  ResendTokenResponse resend(User user);

  void verify(String token);
}
