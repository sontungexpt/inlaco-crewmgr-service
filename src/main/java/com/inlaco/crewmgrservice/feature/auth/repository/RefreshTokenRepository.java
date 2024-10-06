package com.inlaco.crewmgrservice.feature.auth.repository;

import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  int deleteByToken(String token);
}
