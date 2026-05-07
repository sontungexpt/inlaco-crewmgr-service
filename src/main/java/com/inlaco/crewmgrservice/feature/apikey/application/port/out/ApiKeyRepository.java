package com.inlaco.crewmgrservice.feature.apikey.application.port.out;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository {
  ApiKey save(ApiKey apiKey);

  Optional<ApiKey> findByKeyId(String keyId);

  Optional<ApiKey> findByKeyIdAndKeySecret(String keyId, String keySecret);

  List<ApiKey> findAll();

  List<ApiKey> findByClientName(String clientName);

  List<ApiKey> findByActive(boolean active);

  List<ApiKey> findByCreatedBy(String createdBy);

  void deleteById(String id);

  boolean existsByKeyId(String keyId);
}
