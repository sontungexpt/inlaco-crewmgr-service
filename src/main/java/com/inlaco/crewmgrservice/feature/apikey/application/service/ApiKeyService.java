package com.inlaco.crewmgrservice.feature.apikey.application.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
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

/**
 * Service implementation for managing API keys in the crew management system.
 *
 * <p>This service provides comprehensive API key management functionality including creation,
 * validation, activation/deactivation, and various query operations. It supports different key
 * types with configurable expiration policies and integrates with TOTP for enhanced security.
 *
 * <p>The service implements business rules for API key lifecycle management and ensures that all
 * operations are properly audited and validated according to security requirements.
 *
 * @author Crew Management Service
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService implements ApiKeyUseCase {

  private final ApiKeyRepository apiKeyRepository;
  private final ApiKeyConfig config;
  private final TotpUseCase totpUseCase;

  /**
   * Creates a new API key with default EXTERNAL type.
   *
   * <p>This method generates a new API key for the specified client and persists it to the
   * repository. The key will have default expiration settings based on the EXTERNAL key type
   * configuration.
   *
   * @param clientName the name of the client requesting the API key
   * @param description optional description of the API key purpose
   * @param createdBy the user or system that created the API key
   * @return the created and persisted ApiKey instance
   */
  @Override
  public ApiKey createApiKey(String clientName, String description, String createdBy) {
    ApiKey apiKey = generateNew(clientName, description, createdBy);
    return apiKeyRepository.save(apiKey);
  }

  /**
   * Generates a new API key instance with specified type.
   *
   * <p>This method creates a new API key with unique identifier and secret, calculates expiration
   * based on the key type, and sets default values. The key is not persisted - use createApiKey()
   * to save it.
   *
   * @param clientName the name of the client requesting the API key
   * @param description optional description of the API key purpose
   * @param createdBy the user or system that created the API key
   * @param type the type of API key to create
   * @return a new ApiKey instance (not persisted)
   */
  public ApiKey generateNew(
      String clientName, String description, String createdBy, ApiKeyType type) {
    log.info("Generating new API key for client: {} with type: {}", clientName, type);
    Instant expiresAt = calculateExpiration(type);

    String keyId = "sk_" + NanoIdUtils.randomNanoId();
    String keySecret =
        NanoIdUtils.randomNanoId(
            NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
            NanoIdUtils.DEFAULT_ALPHABET,
            NanoIdUtils.DEFAULT_SIZE * 2);

    return ApiKey.builder()
        .keyId(keyId)
        .keySecret(keySecret)
        .clientName(clientName)
        .description(description)
        .active(true)
        .createdAt(Instant.now())
        .expiresAt(expiresAt)
        .createdBy(createdBy)
        .type(type)
        .build();
  }

  /**
   * Generates a new API key with default EXTERNAL type.
   *
   * <p>This is a convenience method that delegates to generateNew() with ApiKeyType.EXTERNAL as the
   * default type.
   *
   * @param clientName the name of the client requesting the API key
   * @param description optional description of the API key purpose
   * @param createdBy the user or system that created the API key
   * @return a new ApiKey instance with EXTERNAL type (not persisted)
   */
  public ApiKey generateNew(String clientName, String description, String createdBy) {
    return generateNew(clientName, description, createdBy, ApiKeyType.EXTERNAL);
  }

  /**
   * Generates a new API key with TOTP verification for enhanced security.
   *
   * <p>This method verifies the provided TOTP code before creating the API key, providing an
   * additional layer of security for sensitive operations. The TOTP verification must be successful
   * for the key to be generated.
   *
   * @param request the API key creation request containing TOTP code and key details
   * @param userId the ID of the user requesting the API key
   * @return the created ApiKey instance
   * @throws SecurityException if TOTP verification fails
   */
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

  /**
   * Verifies TOTP code for viewing secret key information.
   *
   * <p>This method provides an additional security layer before allowing users to view sensitive
   * API key secrets. The TOTP verification ensures that only authorized users can access secret
   * information.
   *
   * @param userId the ID of the user requesting to view the secret
   * @param totpCode the TOTP code provided by the user
   * @param purposeId the purpose identifier for this verification session
   * @return true if verification is successful
   * @throws SecurityException if TOTP verification fails
   */
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

  /**
   * Calculates expiration date for API key based on its type.
   *
   * <p>This method uses the configuration settings to determine the appropriate expiration duration
   * for the specified key type and calculates the absolute expiration timestamp from the current
   * time.
   *
   * @param type the API key type to calculate expiration for
   * @return Instant representing when the key will expire
   */
  public Instant calculateExpiration(ApiKeyType type) {
    var expiration = config.getExpiration();
    var typeConfig = getTypeConfig(type, expiration);

    return Instant.now().plus(typeConfig.getDuration());
  }

  /**
   * Checks if an API key type is renewable based on configuration.
   *
   * <p>This method determines whether keys of the specified type can be renewed when they approach
   * expiration, based on the system configuration settings.
   *
   * @param type the API key type to check for renewability
   * @return true if the key type is renewable, false otherwise
   */
  public boolean isRenewable(ApiKeyType type) {
    var expiration = config.getExpiration();
    return getTypeConfig(type, expiration).isRenewable();
  }

  /**
   * Retrieves expiration configuration for a specific API key type.
   *
   * <p>This method looks up the expiration configuration for the given key type, falling back to
   * default configuration if no specific configuration exists.
   *
   * @param type the API key type to get configuration for
   * @param expiration the expiration configuration container
   * @return the expiration configuration for the specified type
   */
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
