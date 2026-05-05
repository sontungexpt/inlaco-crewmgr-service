package com.inlaco.crewmgrservice.feature.apikey.domain.service;

import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyExpirationConfig;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyDomainService {

  private final ApiKeyExpirationConfig expirationConfig;

  public ApiKey generateNew(String clientName, String description, String createdBy, ApiKeyType type) {
    log.info("Generating new API key for client: {} with type: {}", clientName, type);
    
    Instant expiresAt = expirationConfig.calculateExpiration(type);
    return ApiKey.generateNew(clientName, description, createdBy, type, expiresAt);
  }

  public ApiKey generateNew(String clientName, String description, String createdBy) {
    return generateNew(clientName, description, createdBy, ApiKeyType.EXTERNAL);
  }
}
