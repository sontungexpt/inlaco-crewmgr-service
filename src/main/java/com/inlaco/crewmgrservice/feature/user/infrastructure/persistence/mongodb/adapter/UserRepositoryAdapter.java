package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.user.application.port.out.UserRepository;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity.UserEntity;
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
  private final UserEntityMapper mapper;
  private final UserEntityMongoRepository repository;

  @Override
  public Optional<User> findById(String id) {
    return repository.findById(id).map(mapper::toUser);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repository.findByUsername(username).map(mapper::toUser);
  }

  @Override
  public Optional<User> findByPubId(String pubId) {
    return repository.findByPubId(pubId).map(mapper::toUser);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repository.existsByUsername(username);
  }

  @Override
  public User save(User user) {
    UserEntity entity;
    if (user.getId() == null) {
      // INSERT
      entity = mapper.toUserEntity(user);
    } else {
      entity =
          repository
              .findById(user.getId())
              .map(
                  existing -> {
                    mapper.updateFromUser(user, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toUserEntity(user));
    }
    return mapper.toUser(repository.save(entity));
  }
}
