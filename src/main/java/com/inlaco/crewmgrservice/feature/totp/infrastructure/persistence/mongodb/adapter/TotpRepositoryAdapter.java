package com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.totp.application.port.out.TotpRepository;
import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.entity.TotpSecretEntity;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.repository.TotpSecretMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TotpRepositoryAdapter implements TotpRepository {

  private final TotpSecretMongoRepository mongoRepository;

  @Override
  public TotpSecret save(TotpSecret totpSecret) {
    TotpSecretEntity entity = TotpSecretEntity.fromDomain(totpSecret);
    TotpSecretEntity savedEntity = mongoRepository.save(entity);
    return savedEntity.toDomain();
  }

  @Override
  public Optional<TotpSecret> findByUserIdAndPurposeAndPurposeId(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {
    return mongoRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .map(TotpSecretEntity::toDomain);
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }

  @Override
  public void deleteByUserIdAndPurposeAndPurposeId(String userId, TotpSecret.TotpPurpose purpose, String purposeId) {
    mongoRepository.findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .ifPresent(entity -> mongoRepository.deleteById(entity.getId()));
  }

  @Override
  public List<TotpSecret> findByCreatedAtBefore(Instant cutoff) {
    return mongoRepository.findByCreatedAtBefore(cutoff).stream()
        .map(TotpSecretEntity::toDomain)
        .toList();
  }
}
