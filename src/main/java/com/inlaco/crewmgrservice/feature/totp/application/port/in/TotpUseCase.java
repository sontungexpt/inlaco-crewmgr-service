package com.inlaco.crewmgrservice.feature.totp.application.port.in;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.presentation.dto.response.TotpSetupResponse;

public interface TotpUseCase {
  
  /**
   * Setup TOTP for specific purpose
   */
  TotpSetupResponse setupTotp(String userId, String email, TotpSecret.TotpPurpose purpose, String purposeId);
  
  /**
   * Verify TOTP code
   */
  boolean verifyTotp(String userId, String code, TotpSecret.TotpPurpose purpose, String purposeId);
  
  /**
   * Get TOTP secret by user and purpose
   */
  TotpSecret getTotpSecret(String userId, TotpSecret.TotpPurpose purpose, String purposeId);
  
  /**
   * Disable TOTP
   */
  void disableTotp(String userId, TotpSecret.TotpPurpose purpose, String purposeId);
  
  /**
   * Clean up old TOTP secrets
   */
  void cleanupExpiredTokens();
}
