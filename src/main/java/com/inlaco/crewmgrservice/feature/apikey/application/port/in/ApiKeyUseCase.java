package com.inlaco.crewmgrservice.feature.apikey.application.port.in;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import java.util.List;

public interface ApiKeyUseCase {
  ApiKey createApiKey(String clientName, String description, String createdBy);

  void deactivateApiKey(String keyId);

  void activateApiKey(String keyId);

  ApiKey validateApiKey(String keyId, String keySecret);

  boolean isApiKeyValid(String keyId, String keySecret);

  List<ApiKey> getAllApiKeys();

  List<ApiKey> getApiKeysByClient(String clientName);

  List<ApiKey> getActiveApiKeys();

  List<ApiKey> getInactiveApiKeys();

  List<ApiKey> getApiKeysByCreator(String createdBy);
}
