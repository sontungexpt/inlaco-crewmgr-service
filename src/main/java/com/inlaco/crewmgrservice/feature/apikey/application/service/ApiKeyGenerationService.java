package com.inlaco.crewmgrservice.feature.apikey.application.service;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import com.inlaco.crewmgrservice.feature.apikey.domain.service.ApiKeyDomainService;
  import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request.CreateApiKeyWithTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyGenerationService {

  private final ApiKeyDomainService domainService;
  private final TotpUseCase totpUseCase;

  public ApiKey generateNew(String clientName, String description, String createdBy, ApiKeyType type) {
    log.info("Generating new API key for client: {} with type: {}", clientName, type);
    
    return domainService.generateNew(clientName, description, createdBy, type);
  }

  public ApiKey generateNew(String clientName, String description, String createdBy) {
    return generateNew(clientName, description, createdBy, ApiKeyType.EXTERNAL);
  }

  /**
   * Generate API key with TOTP verification
   */
  public ApiKey generateNewWithTotp(CreateApiKeyWithTotpRequest request, String userId) {
    log.info("Generating new API key with TOTP verification for client: {}", request.getClientName());
    
    // Verify TOTP first
    boolean totpValid = totpUseCase.verifyTotp(
        userId, 
        request.getTotpCode(), 
        TotpSecret.TotpPurpose.API_KEY_CREATION, 
        request.getPurposeId()
    );
    
    if (!totpValid) {
      log.warn("Invalid TOTP code for user: {}", userId);
      throw new IllegalArgumentException("Invalid TOTP code");
    }
    
    log.info("TOTP verification successful for user: {}", userId);
    
    // Generate API key
    return domainService.generateNew(
        request.getClientName(), 
        request.getDescription(), 
        userId, 
        request.getType() != null ? request.getType() : ApiKeyType.EXTERNAL
    );
  }

  /**
   * Verify TOTP for secret key viewing
   */
  public boolean verifyTotpForSecretViewing(String userId, String totpCode, String purposeId) {
    log.info("Verifying TOTP for secret key viewing for user: {}", userId);
    
    boolean totpValid = totpUseCase.verifyTotp(
        userId, 
        totpCode, 
        TotpSecret.TotpPurpose.SECRET_KEY_VIEWING, 
        purposeId
    );
    
    if (totpValid) {
      log.info("TOTP verification successful for secret key viewing for user: {}", userId);
    } else {
      log.warn("Invalid TOTP code for secret key viewing for user: {}", userId);
    }
    
    return totpValid;
  }
}
