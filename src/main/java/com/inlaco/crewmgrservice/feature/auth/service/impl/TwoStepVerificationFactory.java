package com.inlaco.crewmgrservice.feature.auth.service.impl;

import com.inlaco.crewmgrservice.feature.auth.dto.ResendTokenResponse;
import com.inlaco.crewmgrservice.feature.auth.service.TwoStepVerificationService;
import com.inlaco.crewmgrservice.feature.user.model.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TwoStepVerificationFactory {

  private final Map<String, TwoStepVerificationService> twoStepVerificationMap;

  public TwoStepVerificationService getTwoStepVerificationService(String type) {
    if (!twoStepVerificationMap.containsKey(type)) {
      throw new IllegalArgumentException("Invalid two step verification type");
    }
    return twoStepVerificationMap.get(type);
  }

  public ResendTokenResponse sendVerificationCode(String type, User user) {
    return getTwoStepVerificationService(type).send(user);
  }

  public void verifyCode(String type, String code) {
    getTwoStepVerificationService(type).verify(code);
  }

  public ResendTokenResponse resendVerificationCode(String type, User user) {
    return getTwoStepVerificationService(type).resend(user);
  }
}
