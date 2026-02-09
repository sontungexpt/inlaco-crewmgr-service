package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.adapter;

import com.inlaco.crewmgrservice.feature.auth.application.port.out.EmailVerificationTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.model.EmailVerificationToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.mapper.EmailVerificationTokenRedisMapper;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.redis.repository.RedisEmailVerificationTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationTokenRepositoryAdapter implements EmailVerificationTokenRepository {
  private final RedisEmailVerificationTokenRepository redisEmailVerificationTokenRepository;
  private final EmailVerificationTokenRedisMapper emailVerificationTokenRedisMapper;

  @Override
  public Optional<EmailVerificationToken> findByHashToken(String hashToken) {
    return redisEmailVerificationTokenRepository
        .findByHashToken(hashToken)
        .map(emailVerificationTokenRedisMapper::toDomain);
  }

  @Override
  public void deleteByHashToken(String token) {
    redisEmailVerificationTokenRepository.deleteByHashToken(token);
  }

  @Override
  public Optional<EmailVerificationToken> findByUserId(String userId) {
    return redisEmailVerificationTokenRepository
        .findByUserId(userId)
        .map(emailVerificationTokenRedisMapper::toDomain);
  }

  @Override
  public void deleteByUserId(String userId) {
    redisEmailVerificationTokenRepository.deleteByUserId(userId);
  }

  @Override
  public EmailVerificationToken save(EmailVerificationToken token) {
    return emailVerificationTokenRedisMapper.toDomain(
        redisEmailVerificationTokenRepository.save(
            emailVerificationTokenRedisMapper.toEntity(token)));
  }

  @Override
  public void deleteById(String userId) {
    redisEmailVerificationTokenRepository.deleteById(userId);
  }
}
