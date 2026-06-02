package com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.otp.application.port.out.OtpRepository;
import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.entity.OtpEntity;
import com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.mapper.OtpEntityMapper;
import com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.repository.OtpMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OtpRepositoryAdapter implements OtpRepository {
  private final OtpMongoRepository repository;
  private final OtpEntityMapper mapper;

  @Override
  public java.util.Optional<Otp> findById(String id) {
    return repository.findById(id).map(mapper::toOtp);
  }

  @Override
  public java.util.Optional<Otp> findByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId) {
    return repository
        .findByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId)
        .map(mapper::toOtp);
  }

  @Override
  public Otp save(Otp otp) {
    OtpEntity entity = mapper.toOtpEntity(otp);
    return mapper.toOtp(repository.save(entity));
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }

  @Override
  public void deleteByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId) {
    repository.deleteByUserIdAndPurposeAndPurposeId(userId, purpose, purposeId);
  }

  @Override
  public void cleanupExpired() {
    repository.deleteByExpiresAtBefore(java.time.Instant.now());
    log.info("Cleaned up expired OTPs");
  }
}
