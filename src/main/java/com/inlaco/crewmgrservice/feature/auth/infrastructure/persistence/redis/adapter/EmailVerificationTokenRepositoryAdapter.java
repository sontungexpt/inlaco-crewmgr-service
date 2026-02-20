package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.adapter;

import com.inlaco.crewmgrservice.feature.auth.application.port.out.EmailVerificationTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.mapper.EmailVerificationTokenMapper;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.repository.EmailVerificationTokenRedisRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationTokenRepositoryAdapter implements EmailVerificationTokenRepository {
  private final EmailVerificationTokenRedisRepository repository;
  private final EmailVerificationTokenMapper mapper;

  @Override
  public Optional<EmailVerificationToken> findByHashToken(String hashToken) {
    return repository.findByHashToken(hashToken).map(mapper::toDomain);
  }

  @Override
  public void deleteByHashToken(String token) {
    repository.deleteByHashToken(token);
  }

  @Override
  public Optional<EmailVerificationToken> findByUserId(String userId) {
    return repository.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public void deleteByUserId(String userId) {
    repository.deleteByUserId(userId);
  }

  @Override
  public EmailVerificationToken save(EmailVerificationToken token) {
    return mapper.toDomain(repository.save(mapper.toEntity(token)));
  }

  @Override
  public void deleteById(String userId) {
    repository.deleteById(userId);
  }
}
