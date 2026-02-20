package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.repository;

import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.entity.EmailVerificationTokenEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRedisRepository
    extends CrudRepository<EmailVerificationTokenEntity, String> {

  Optional<EmailVerificationTokenEntity> findByHashToken(String hashToken);

  void deleteByHashToken(String token);

  Optional<EmailVerificationTokenEntity> findByUserId(String userId);

  void deleteByUserId(String userId);
}
