package com.inlaco.crewmgrservice.feature.apikey.application.service;

import com.inlaco.crewmgrservice.feature.apikey.application.port.in.ApiKeyUseCase;
import com.inlaco.crewmgrservice.feature.apikey.application.port.out.ApiKeyRepository;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService implements ApiKeyUseCase {

  private final ApiKeyRepository apiKeyRepository;

  @Override
  public ApiKey createApiKey(String clientName, String description, String createdBy) {
    ApiKey apiKey = ApiKey.generateNew(clientName, description, createdBy);
    return apiKeyRepository.save(apiKey);
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
