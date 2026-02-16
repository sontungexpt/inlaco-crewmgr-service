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

  private final RefreshTokenMongoRepository repository;
  private final RefreshTokenEntityMapper mapper;

  @Override
  public Optional<RefreshToken> findByHashedToken(String token) {
    return repository.findByHashedToken(token).map(mapper::toDomain);
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return mapper.toDomain(repository.save(mapper.toEntity(refreshToken)));
  }
}
