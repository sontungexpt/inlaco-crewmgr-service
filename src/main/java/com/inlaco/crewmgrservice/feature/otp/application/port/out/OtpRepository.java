package com.inlaco.crewmgrservice.feature.otp.application.port.out;

import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import java.util.Optional;

public interface OtpRepository {

  Optional<Otp> findById(String id);

  Optional<Otp> findByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId);

  Otp save(Otp otp);

  void deleteById(String id);

  void deleteByUserIdAndPurposeAndPurposeId(
      String userId, Otp.OtpPurpose purpose, String purposeId);

  void cleanupExpired();
}
