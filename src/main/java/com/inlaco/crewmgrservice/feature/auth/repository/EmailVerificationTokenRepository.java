package com.inlaco.crewmgrservice.feature.auth.repository;

import com.inlaco.crewmgrservice.feature.auth.model.EmailVerificationToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository
    extends CrudRepository<EmailVerificationToken, String> {

  Optional<EmailVerificationToken> findByHashToken(String hashToken);

  void deleteByHashToken(String token);

  void deleteByUserId(String userId);

  Optional<EmailVerificationToken> findByUserId(String userPubId);

  Optional<EmailVerificationToken> findByResendToken(String resendToken);
}
