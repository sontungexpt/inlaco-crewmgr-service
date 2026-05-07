package com.inlaco.crewmgrservice.feature.apikey.application.service;

import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
import com.inlaco.crewmgrservice.feature.apikey.application.port.out.ApiKeyRepository;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyConfig;
import com.inlaco.crewmgrservice.feature.apikey.presentation.dto.request.CreateApiKeyWithTotpRequest;
import com.inlaco.crewmgrservice.feature.totp.application.port.in.TotpUseCase;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService implements ApiKeyUseCase {

  private final ApiKeyRepository apiKeyRepository;
  private final ApiKeyConfig config;
  private final TotpUseCase totpUseCase;

  @Override
  public ApiKey createApiKey(String clientName, String description, String createdBy) {
    ApiKey apiKey = generateNew(clientName, description, createdBy);
    return apiKeyRepository.save(apiKey);
  }

  public ApiKey generateNew(
      String clientName, String description, String createdBy, ApiKeyType type) {
    log.info("Generating new API key for client: {} with type: {}", clientName, type);
    Instant expiresAt = calculateExpiration(type);
    return ApiKey.generateNew(clientName, description, createdBy, type, expiresAt);
  }

  public ApiKey generateNew(String clientName, String description, String createdBy) {
    return generateNew(clientName, description, createdBy, ApiKeyType.EXTERNAL);
  }

  /** Generate API key with TOTP verification */
  public ApiKey generateNewWithTotp(CreateApiKeyWithTotpRequest request, String userId) {
    log.info(
        "Generating new API key with TOTP verification for client: {}", request.getClientName());

    // Verify TOTP first - let exceptions bubble up to advice
    totpUseCase.verifyTotp(
        userId,
        request.getTotpCode(),
        TotpSecret.TotpPurpose.API_KEY_CREATION,
        request.getPurposeId());

    log.info("TOTP verification successful for user: {}", userId);

    // Generate API key
    return generateNew(
        request.getClientName(),
        request.getDescription(),
        userId,
        request.getType() != null ? request.getType() : ApiKeyType.EXTERNAL);
  }

  /** Verify TOTP for secret key viewing */
  public boolean verifyTotpForSecretViewing(String userId, String totpCode, String purposeId) {
    log.info("Verifying TOTP for secret key viewing for user: {}", userId);

    try {
      totpUseCase.verifyTotp(
          userId, totpCode, TotpSecret.TotpPurpose.SECRET_KEY_VIEWING, purposeId);

      log.info("TOTP verification successful for secret key viewing for user: {}", userId);
      return true;
    } catch (Exception e) {
      log.warn("Invalid TOTP code for secret key viewing for user: {}", userId);
      throw e; // Re-throw to be handled by advice
    }
  }

  public Instant calculateExpiration(ApiKeyType type) {
    var expiration = config.getExpiration();
    var typeConfig = getTypeConfig(type, expiration);

    return Instant.now().plus(typeConfig.getDuration());
  }

  public boolean isRenewable(ApiKeyType type) {
    var expiration = config.getExpiration();
    return getTypeConfig(type, expiration).isRenewable();
  }

  private ApiKeyConfig.ExpirationConfig getTypeConfig(
      ApiKeyType type, ApiKeyConfig.Expiration expiration) {
    Map<ApiKeyType, ApiKeyConfig.ExpirationConfig> map = expiration.getByType();
    if (map != null && map.containsKey(type)) {
      return map.get(type);
    }

    return new ApiKeyConfig.ExpirationConfig(
        expiration.getDefaultValue(), expiration.isDefaultRenewable());
  }

  @Override
  public void deactivateApiKey(String keyId) {
    apiKeyRepository
        .findByKeyId(keyId)
        .ifPresent(
            apiKey -> {
              apiKey.deactivate();
              apiKeyRepository.save(apiKey);
            });
  }

  @Override
  public void activateApiKey(String keyId) {
    apiKeyRepository
        .findByKeyId(keyId)
        .ifPresent(
            apiKey -> {
              apiKey.activate();
              apiKeyRepository.save(apiKey);
            });
  }

  @Override
  public ApiKey validateApiKey(String keyId, String keySecret) {
    return apiKeyRepository
        .findByKeyIdAndKeySecret(keyId, keySecret)
        .filter(ApiKey::isValid)
        .orElse(null);
  }

  @Override
  public boolean isApiKeyValid(String keyId, String keySecret) {
    return validateApiKey(keyId, keySecret) != null;
  }

  @Override
  public List<ApiKey> getAllApiKeys() {
    return apiKeyRepository.findAll();
  }

  @Override
  public List<ApiKey> getApiKeysByClient(String clientName) {
    return apiKeyRepository.findByClientName(clientName);
  }

  @Override
  public List<ApiKey> getActiveApiKeys() {
    return apiKeyRepository.findByActive(true);
  }

  @Override
  public List<ApiKey> getInactiveApiKeys() {
    return apiKeyRepository.findByActive(false);
  }

  @Override
  public List<ApiKey> getApiKeysByCreator(String createdBy) {
    return apiKeyRepository.findByCreatedBy(createdBy);
  }
}
