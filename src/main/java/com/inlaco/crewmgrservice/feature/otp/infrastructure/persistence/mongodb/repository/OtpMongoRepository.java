package com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.entity.OtpEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpMongoRepository extends MongoRepository<OtpEntity, String> {

  Optional<OtpEntity> findByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId);

  void deleteByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId);

  void deleteByExpiresAtBefore(java.time.Instant cutoff);
}
