package com.inlaco.crewmgrservice.feature.totp.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotpSetupResponse {
  
  private String secret;
  private String qrCode;
  private String purposeId;
  private TotpSecret.TotpPurpose purpose;
  private String instructions;
}
