package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.mapper.UserEntityMapper;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.repository.UserEntityMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryAdapter implements UserRepository {
  private final UserEntityMapper userEntityMapper;
  private final UserEntityMongoRepository userEntityMongoRepository;

  @Override
  public Optional<User> findById(String id) {
    return userEntityMongoRepository.findById(id).map(userEntityMapper::toUser);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userEntityMongoRepository.findByUsername(username).map(userEntityMapper::toUser);
  }

  @Override
  public Optional<User> findByPubId(String pubId) {
    return userEntityMongoRepository.findByPubId(pubId).map(userEntityMapper::toUser);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userEntityMongoRepository.existsByUsername(username);
  }

  @Override
  public User save(User user) {
    return userEntityMapper.toUser(
        userEntityMongoRepository.save(userEntityMapper.toUserEntity(user)));
  }
}
