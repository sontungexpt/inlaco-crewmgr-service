package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.auth.application.port.out.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.mapper.RefreshTokenMapper;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.repository.RefreshTokenMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

  private final RefreshTokenMongoRepository refreshTokenMongoRepository;
  private final RefreshTokenMapper refreshTokenMapper;

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenMongoRepository.findByToken(token).map(refreshTokenMapper::toDomain);
  }

  @Override
  public int deleteByToken(String token) {
    return refreshTokenMongoRepository.deleteByToken(token);
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenMapper.toDomain(
        refreshTokenMongoRepository.save(refreshTokenMapper.toEntity(refreshToken)));
  }
}
