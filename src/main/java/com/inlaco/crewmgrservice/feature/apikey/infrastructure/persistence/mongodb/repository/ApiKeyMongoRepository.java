package com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity.ApiKeyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyMongoRepository extends MongoRepository<ApiKeyEntity, String> {
  Optional<ApiKeyEntity> findByKeyId(String keyId);

  Optional<ApiKeyEntity> findByKeyIdAndKeySecret(String keyId, String keySecret);

  List<ApiKeyEntity> findByClientName(String clientName);

  List<ApiKeyEntity> findByActive(boolean active);

  List<ApiKeyEntity> findByCreatedBy(String createdBy);

  boolean existsByKeyId(String keyId);
}
