package com.inlaco.crewmgrservice.feature.auth.application.port.out;

import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import java.util.Optional;

public interface EmailVerificationTokenRepository {

  void deleteById(String userId);

  EmailVerificationToken save(EmailVerificationToken token);

  Optional<EmailVerificationToken> findByHashToken(String hashToken);

  void deleteByHashToken(String token);

  Optional<EmailVerificationToken> findByUserId(String userId);

  void deleteByUserId(String userId);
}
