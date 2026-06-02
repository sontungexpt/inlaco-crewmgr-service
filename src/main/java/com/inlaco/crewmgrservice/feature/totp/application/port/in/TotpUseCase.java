package com.inlaco.crewmgrservice.feature.totp.application.port.in;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;
import java.time.Duration;

public interface TotpUseCase {

  TotpSetupResponse setupTotp(
      String userId, String email, TotpSecret.TotpPurpose purpose, String purposeId);

  boolean verifyTotp(String userId, String code, TotpSecret.TotpPurpose purpose, String purposeId);

  TotpSecret getTotpSecret(String userId, TotpSecret.TotpPurpose purpose, String purposeId);

  void disableTotp(String userId, TotpSecret.TotpPurpose purpose, String purposeId);

  void cleanupExpiredTokens(Duration lifetime);
}
