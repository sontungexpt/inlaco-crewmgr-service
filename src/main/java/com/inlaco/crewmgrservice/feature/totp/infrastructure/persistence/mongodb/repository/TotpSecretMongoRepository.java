package com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.entity.TotpSecretEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotpSecretMongoRepository extends MongoRepository<TotpSecretEntity, String> {

  Optional<TotpSecretEntity> findByUserIdAndPurposeAndPurposeId(
      String userId, TotpSecret.TotpPurpose purpose, String purposeId);

  List<TotpSecretEntity> findByCreatedAtBefore(Instant cutoff);
}
