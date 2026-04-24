package com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.notify.application.port.out.DeviceTokenRepostiory;
import com.inlaco.crewmgrservice.feature.notify.domain.model.DeviceToken;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.mapper.DeviceTokenEntityMapper;
import com.inlaco.crewmgrservice.feature.notify.infrastructure.persistence.mongodb.repository.DeviceTokenMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRepositoryAdapter implements DeviceTokenRepostiory {

  private final DeviceTokenMongoRepository repository;
  private final DeviceTokenEntityMapper mapper;

  @Override
  public DeviceToken save(DeviceToken deviceToken) {
    var deviceTokenEntity = mapper.toDeviceTokenEntity(deviceToken);
    return mapper.toDeviceToken(repository.save(deviceTokenEntity));
  }

  @Override
  public void deleteByToken(String deviceToken) {
    repository.deleteByToken(deviceToken);
  }

  @Override
  public Optional<DeviceToken> findByToken(String token) {
    return repository.findByToken(token).map(mapper::toDeviceToken);
  }

  @Override
  public List<DeviceToken> findByUserId(String userId) {
    return repository.findByUserId(new ObjectId(userId)).stream()
        .map(mapper::toDeviceToken)
        .toList();
  }

  @Override
  public boolean existsByToken(String token) {
    return repository.existsByToken(token);
  }
}
