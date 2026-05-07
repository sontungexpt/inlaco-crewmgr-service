package com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.apikey.application.port.out.ApiKeyRepository;
import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKey;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity.ApiKeyEntity;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.mapper.ApiKeyEntityMapper;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.repository.ApiKeyMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApiKeyRepositoryAdapter implements ApiKeyRepository {

  private final ApiKeyMongoRepository mongoRepository;
  private final ApiKeyEntityMapper entityMapper;

  @Override
  public ApiKey save(ApiKey apiKey) {
    ApiKeyEntity entity = entityMapper.toApiKeyEntity(apiKey);
    ApiKeyEntity saved = mongoRepository.save(entity);
    return entityMapper.toApiKey(saved);
  }

  @Override
  public Optional<ApiKey> findByKeyId(String keyId) {
    return mongoRepository.findByKeyId(keyId).map(entityMapper::toApiKey);
  }

  @Override
  public Optional<ApiKey> findByKeyIdAndKeySecret(String keyId, String keySecret) {
    return mongoRepository.findByKeyIdAndKeySecret(keyId, keySecret).map(entityMapper::toApiKey);
  }

  @Override
  public List<ApiKey> findAll() {
    return mongoRepository.findAll().stream().map(entityMapper::toApiKey).toList();
  }

  @Override
  public List<ApiKey> findByClientName(String clientName) {
    return mongoRepository.findByClientName(clientName).stream()
        .map(entityMapper::toApiKey)
        .toList();
  }

  @Override
  public List<ApiKey> findByActive(boolean active) {
    return mongoRepository.findByActive(active).stream().map(entityMapper::toApiKey).toList();
  }

  @Override
  public List<ApiKey> findByCreatedBy(String createdBy) {
    return mongoRepository.findByCreatedBy(createdBy).stream().map(entityMapper::toApiKey).toList();
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }

  @Override
  public boolean existsByKeyId(String keyId) {
    return mongoRepository.existsByKeyId(keyId);
  }
}
