package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.auth.application.port.out.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.domain.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.mapper.RefreshTokenEntityMapper;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.repository.RefreshTokenMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

  private final RefreshTokenMongoRepository refreshTokenMongoRepository;
  private final RefreshTokenEntityMapper refreshTokenMapper;

  @Override
  public Optional<RefreshToken> findByHashedToken(String token) {
    return refreshTokenMongoRepository.findByHashedToken(token).map(refreshTokenMapper::toDomain);
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenMapper.toDomain(
        refreshTokenMongoRepository.save(refreshTokenMapper.toEntity(refreshToken)));
  }
}
