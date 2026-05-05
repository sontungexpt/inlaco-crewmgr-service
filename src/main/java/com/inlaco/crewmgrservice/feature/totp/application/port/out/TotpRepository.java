package com.inlaco.crewmgrservice.feature.totp.application.port.out;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TotpRepository {
  
  TotpSecret save(TotpSecret totpSecret);
  
  Optional<TotpSecret> findByUserIdAndPurposeAndPurposeId(String userId, TotpSecret.TotpPurpose purpose, String purposeId);
  
  void deleteById(String id);
  
  void deleteByUserIdAndPurposeAndPurposeId(String userId, TotpSecret.TotpPurpose purpose, String purposeId);
  
  List<TotpSecret> findByCreatedAtBefore(Instant cutoff);
}
